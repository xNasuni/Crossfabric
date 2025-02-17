package dev.xnasuni.crossfabric.multiversion;

import dev.xnasuni.crossfabric.Constants;
import dev.xnasuni.crossfabric.annotation.VersionedMixin;
import dev.xnasuni.crossfabric.tools.ClassUtils;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import static dev.xnasuni.crossfabric.Constants.LOGGER;

public class VersionedMixinPluginImpl implements IMixinConfigPlugin {
    @Override public boolean shouldApplyMixin(String className, String mixinClass) {
        try {
            ClassNode target = MixinService.getService().getBytecodeProvider().getClassNode(className);
            ClassNode mixin = MixinService.getService().getBytecodeProvider().getClassNode(mixinClass);

            if (mixin.visibleAnnotations == null) {
                return true;
            }

            for (AnnotationNode node : mixin.visibleAnnotations) {
                if (Type.getDescriptor(VersionedMixin.class).equals(node.desc)) {
                    List<String> conditions = Annotations.getValue(node, "value");
                    String currentVersion = Constants.getMinecraftVersion();

                    boolean testPassed = true;

                    for (String conditionString : conditions) {
                        VersionString condition = VersionString.of(conditionString);
                        testPassed = testPassed && condition.test(currentVersion);
                    }

                    if (testPassed) {
                        LOGGER.info("MultiVersion applying mixin {} patching {} with version range [{}]", mixin.name, target.name, ClassUtils.joinSeparator(conditions));
                    }

                    return testPassed;
                }
            }
        } catch (Throwable e) {
            LOGGER.error("Exception while trying to check MultiVersion mixin, not applying.", e);
            return false;
        }

        return true;
    }

    @Override public void onLoad(String mixinPackage) { }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public List<String> getMixins() {  return null; }
    @Override public String getRefMapperConfig() { return null; }
}