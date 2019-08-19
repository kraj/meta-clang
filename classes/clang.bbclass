# Add the necessary override
CC_toolchain-clang  = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang++ ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -E"
CCLD_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CLANG_TIDY_EXE_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang-tidy ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
RANLIB_toolchain-clang = "${HOST_PREFIX}llvm-ranlib"
AR_toolchain-clang = "${HOST_PREFIX}llvm-ar"
NM_toolchain-clang = "${HOST_PREFIX}llvm-nm"

COMPILER_RT ??= "--rtlib=compiler-rt ${UNWINDLIB}"

UNWINDLIB ??= "--unwindlib=libunwind"
UNWINDLIB_riscv64 = "--unwindlib=libgcc"
UNWINDLIB_riscv32 = "--unwindlib=libgcc"
UNWINDLIB_powerpc = "--unwindlib=libgcc"

LIBCPLUSPLUS ??= "--stdlib=libc++"

COMPILER_RT_toolchain-gcc = ""
LIBCPLUSPLUS_toolchain-gcc = ""

TARGET_CXXFLAGS_append_toolchain-clang = " ${LIBCPLUSPLUS}"
TUNE_CCARGS_append_toolchain-clang = " ${COMPILER_RT} ${LIBCPLUSPLUS}"

THUMB_TUNE_CCARGS_remove_toolchain-clang = "-mthumb-interwork"
TUNE_CCARGS_remove_toolchain-clang = "-meb"
TUNE_CCARGS_remove_toolchain-clang = "-mel"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"

TUNE_CCARGS_remove_toolchain-clang_powerpc = "-mhard-float"
TUNE_CCARGS_remove_toolchain-clang_powerpc = "-mno-spe"

TUNE_CCARGS_append_toolchain-clang = " -Wno-error=unused-command-line-argument -Qunused-arguments"

LDFLAGS_append_toolchain-clang_class-nativesdk_x86-64 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux-x86-64.so.2"
LDFLAGS_append_toolchain-clang_class-nativesdk_x86 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux.so.2"
LDFLAGS_append_toolchain-clang_class-nativesdk_aarch64 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux-aarch64.so.1"

LDFLAGS_toolchain-clang_class-nativesdk = "${BUILDSDK_LDFLAGS} \
                                           -Wl,-rpath-link,${STAGING_LIBDIR}/.. \
                                           -Wl,-rpath,${libdir}/.. "

# Enable lld globally"
LDFLAGS_append_toolchain-clang = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-lld', ' -fuse-ld=lld', '', d)}"

# choose between 'gcc' 'clang' an empty '' can be used as well
TOOLCHAIN ??= "gcc"

TOOLCHAIN_class-native = "gcc"
TOOLCHAIN_class-nativesdk = "gcc"
TOOLCHAIN_class-cross-canadian = "gcc"
TOOLCHAIN_class-crosssdk = "gcc"
TOOLCHAIN_class-cross = "gcc"

OVERRIDES =. "${@['', 'toolchain-${TOOLCHAIN}:']['${TOOLCHAIN}' != '']}"
OVERRIDES[vardepsexclude] += "TOOLCHAIN"

#DEPENDS_append_toolchain-clang_class-target = " clang-cross-${TARGET_ARCH} "
#DEPENDS_remove_toolchain-clang_allarch = "clang-cross-${TARGET_ARCH}"

def clang_dep_prepend(d):
    if not d.getVar('INHIBIT_DEFAULT_DEPS', False):
        if not oe.utils.inherits(d, 'allarch') :
            return " clang-cross-${TARGET_ARCH} compiler-rt libcxx"
    return ""

BASEDEPENDS_remove_toolchain-clang_class-target = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}compilerlibs"
BASEDEPENDS_append_toolchain-clang_class-target = "${@clang_dep_prepend(d)}"

PREFERRED_PROVIDER_libunwind_toolchain-clang = "libcxx"
PREFERRED_PROVIDER_libunwind ?= "libunwind"
PREFERRED_PROVIDER_libunwind_powerpc = "libunwind"
PREFERRED_PROVIDER_libunwind_riscv32 = "libunwind"
PREFERRED_PROVIDER_libunwind_riscv64 = "libunwind"
