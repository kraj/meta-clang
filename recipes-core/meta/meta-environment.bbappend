export TARGET_CLANGCC_ARCH = "${TARGET_CC_ARCH}"
TARGET_CLANGCC_ARCH_remove = "-mthumb-interwork"
TARGET_CLANGCC_ARCH_remove = "-mmusl"
TARGET_CLANGCC_ARCH_remove = "-muclibc"
TARGET_CLANGCC_ARCH_remove = "-meb"
TARGET_CLANGCC_ARCH_remove = "-mel"
TARGET_CLANGCC_ARCH_append = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"
TARGET_CLANGCC_ARCH_remove_powerpc = "-mhard-float"
TARGET_CLANGCC_ARCH_remove_powerpc = "-mno-spe"

create_sdk_files_append() {
        script=${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
        if ${@bb.utils.contains('CLANGSDK', '1', 'true', 'false', d)}; then
            echo 'export CLANGCC="${TARGET_PREFIX}clang ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
            echo 'export CLANGCXX="${TARGET_PREFIX}clang++ ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
            echo 'export CLANGCPP="${TARGET_PREFIX}clang -E ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
            echo 'export CLANG_TIDY_EXE="${TARGET_PREFIX}clang-tidy"' >> $script
        fi
}
