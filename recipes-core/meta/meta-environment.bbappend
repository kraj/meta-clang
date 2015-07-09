export TARGET_CLANGCC_ARCH = "${TARGET_CC_ARCH}"
TARGET_CLANGCC_ARCH_remove = "-mthumb-interwork"
create_sdk_files_append() {
        script=${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
        echo 'export CLANGCC="${TARGET_PREFIX}clang ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
        echo 'export CLANGCXX="${TARGET_PREFIX}clang++ ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
        echo 'export CLANGCPP="${TARGET_PREFIX}clang -E ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
}
