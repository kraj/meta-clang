export TARGET_CLANGCC_ARCH = "${TARGET_CC_ARCH}"
TARGET_CLANGCC_ARCH_remove = "-mthumb-interwork"
TARGET_CLANGCC_ARCH_remove = "-mmusl"
TARGET_CLANGCC_ARCH_remove = "-muclibc"
TARGET_CLANGCC_ARCH_remove = "-march=armv7ve"
TARGET_CLANGCC_ARCH_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "armv7ve", " -march=armv7a", "", d)}"
TARGET_CLANGCC_ARCH_remove_toolchain-clang = "-meb"
TARGET_CLANGCC_ARCH_remove_toolchain-clang = "-mel"
TARGET_CLANGCC_ARCH_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"
TARGET_CLANGCC_ARCH_remove_toolchain-clang_powerpc = "-mhard-float"
TARGET_CLANGCC_ARCH_remove_toolchain-clang_powerpc = "-mno-spe"

create_sdk_files_append() {
        script=${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
        echo 'export CLANGCC="${TARGET_PREFIX}clang ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
        echo 'export CLANGCXX="${TARGET_PREFIX}clang++ ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
        echo 'export CLANGCPP="${TARGET_PREFIX}clang -E ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
}
