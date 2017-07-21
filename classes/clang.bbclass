# Add the necessary override
CC_toolchain-clang  = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang++ ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -E"
CCLD_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"

SECURITY_PIE_CFLAGS_toolchain-clang= "-pie -fPIE"

THUMB_TUNE_CCARGS_remove_toolchain-clang = "-mthumb-interwork"
TUNE_CCARGS_remove_toolchain-clang = "-meb"
TUNE_CCARGS_remove_toolchain-clang = "-mel"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"
# Clang/llvm doesnt support armv7ve tunes yet
TUNE_CCARGS_remove_toolchain-clang = "-march=armv7ve"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "armv7ve", " -march=armv7a", "", d)}"

TUNE_CCARGS_remove_toolchain-clang_powerpc = "-mhard-float"
TUNE_CCARGS_remove_toolchain-clang_powerpc = "-mno-spe"

TUNE_CCARGS_append_toolchain-clang = " -D__extern_always_inline=inline -no-integrated-as -Wno-error=unused-command-line-argument -Qunused-arguments"

TOOLCHAIN_OPTIONS_append_toolchain-clang_class-nativesdk_x86-64 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux-x86-64.so.2"
TOOLCHAIN_OPTIONS_append_toolchain-clang_class-nativesdk_x86 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux.so.2"

# choose between 'gcc' 'clang' an empty '' can be used as well
TOOLCHAIN ??= "gcc"

TOOLCHAIN_class-native = "gcc"
TOOLCHAIN_class-nativesdk = "gcc"
TOOLCHAIN_class-cross-canadian = "gcc"
TOOLCHAIN_class-crosssdk = "gcc"
TOOLCHAIN_class-cross = "gcc"

OVERRIDES .= "${@['', ':toolchain-${TOOLCHAIN}']['${TOOLCHAIN}' != '']}"
OVERRIDES[vardepsexclude] += "TOOLCHAIN"

#DEPENDS_append_toolchain-clang_class-target = " clang-cross-${TARGET_ARCH} "
#DEPENDS_remove_toolchain-clang_allarch = "clang-cross-${TARGET_ARCH}"

def clang_dep_prepend(d):
    if not d.getVar('INHIBIT_DEFAULT_DEPS', False):
        if not oe.utils.inherits(d, 'allarch') :
            return " clang-cross-${TARGET_ARCH} compiler-rt libcxx llvm-libunwind"
    return ""

BASEDEPENDS_remove_toolchain-clang_class-target = "virtual/${TARGET_PREFIX}gcc"
BASEDEPENDS_remove_toolchain-clang_class-target_mipsarch = "llvm-libunwind"
BASEDEPENDS_append_toolchain-clang_class-target = "${@clang_dep_prepend(d)}"

PREFERRED_PROVIDER_libunwind = "libunwind"
PREFERRED_PROVIDER_libunwind_toolchain-clang = "llvm-libunwind"
