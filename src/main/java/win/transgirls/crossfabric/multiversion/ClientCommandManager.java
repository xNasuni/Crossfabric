package win.transgirls.crossfabric.multiversion;

import com.mojang.brigadier.CommandDispatcher;
import win.transgirls.crossfabric.Constants;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;
import static win.transgirls.crossfabric.Constants.LOGGER;

public class ClientCommandManager {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    // register commands via reflection for better compatibility
    @SuppressWarnings("unchecked")
    public static void register(Consumer<CommandDispatcher<Object>> func) {
        String caller = new Exception().getStackTrace()[1].getClassName().replaceAll("\\.", "/");

        try { // (greater than) >=1.19 register commands
            Class<?> clientCommandRegistrationCallbackClass = Class.forName("net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback");

            Field eventField = clientCommandRegistrationCallbackClass.getDeclaredField("EVENT");
            eventField.setAccessible(true);

            Object event = eventField.get(null);

            Class<?> clientCommandRegistrationCallbackInterface = Class.forName("net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback");
            InvocationHandler lambdaHandler = (proxy, method, args) -> {
                if (method.getName().equals("register")) {
                    CommandDispatcher<Object> dispatcher = (CommandDispatcher<Object>) args[0];
                    func.accept(dispatcher);
                } else if (method.getName().equals("equals")) {
                    return proxy == args[0];
                } else if (method.getName().equals("hashCode")) {
                    return System.identityHashCode(proxy);
                } else if (method.getName().equals("toString")) {
                    return "Proxy for " + clientCommandRegistrationCallbackInterface.getName(); // make a safe string
                }
                return null;
            };

            Object callback = Proxy.newProxyInstance(
                    clientCommandRegistrationCallbackInterface.getClassLoader(),
                    new Class<?>[]{clientCommandRegistrationCallbackInterface},
                    lambdaHandler
            );

            Method registerMethod = event.getClass().getDeclaredMethod("register", Object.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(event, callback);
        } catch (Throwable e) { // (less than) <=1.18.2 register commands fallback
            try {
                Class<?> clientCommandManager = Class.forName("net.fabricmc.fabric.api.client.command.v1.ClientCommandManager");
                Field dispatcherField = clientCommandManager.getDeclaredField("DISPATCHER");
                dispatcherField.setAccessible(true);

                Object dispatcher = dispatcherField.get(null);
                if (dispatcher instanceof CommandDispatcher) {
                    func.accept((CommandDispatcher<Object>) dispatcher);
                }
            } catch (Throwable e2) { // last fallback
                LOGGER.error("All command registration attempts failed.\nThis is a bug!! Please report this to the developer at https://github.com/xNasuni/Crossfabric/issues\nRunning Minecraft {}, Exceptions: {} {}", Constants.getMinecraftVersion(), e, e2);
                return;
            }
        }

        LOGGER.info("[{}] Successfully registered commands for {}", Constants.getMinecraftVersion(), caller);
    }
}