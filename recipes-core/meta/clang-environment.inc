export TARGET_CLANGCC_ARCH = "${TARGET_CC_ARCH}"
TARGET_CLANGCC_ARCH:remove = "-mthumb-interwork"
TARGET_CLANGCC_ARCH:remove = "-mmusl"
TARGET_CLANGCC_ARCH:remove = "-muclibc"
TARGET_CLANGCC_ARCH:remove = "-meb"
TARGET_CLANGCC_ARCH:remove = "-mel"
TARGET_CLANGCC_ARCH:append = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"
TARGET_CLANGCC_ARCH:remove:powerpc = "-mhard-float"
TARGET_CLANGCC_ARCH:remove:powerpc = "-mno-spe"

create_sdk_files:append() {
        script=${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
        if ${@bb.utils.contains('CLANGSDK', '1', 'true', 'false', d)}; then
            echo 'export CLANGCC="${TARGET_PREFIX}clang --target=${TARGET_SYS} ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
            echo 'export CLANGCXX="${TARGET_PREFIX}clang++ --target=${TARGET_SYS} ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
            echo 'export CLANGCPP="${TARGET_PREFIX}clang -E --target=${TARGET_SYS} ${TARGET_CLANGCC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
            echo 'export CLANG_TIDY_EXE="${TARGET_PREFIX}clang-tidy"' >> $script
        fi
}
