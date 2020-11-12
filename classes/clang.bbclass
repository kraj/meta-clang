# Add the necessary override
CCACHE_COMPILERCHECK_toolchain-clang = "%compiler% -v"
HOST_CC_ARCH_prepend_toolchain-clang = "-target ${HOST_SYS} "
CC_toolchain-clang  = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang++ ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -E"
CCLD_toolchain-clang = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
RANLIB_toolchain-clang = "${HOST_PREFIX}llvm-ranlib"
AR_toolchain-clang = "${HOST_PREFIX}llvm-ar"
NM_toolchain-clang = "${HOST_PREFIX}llvm-nm"

LTO_toolchain-clang = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', '-flto=thin', '-flto -fuse-ld=lld', d)}"
PACKAGE_DEBUG_SPLIT_STYLE_toolchain-clang = "debug-without-src"

export CLANG_TIDY_toolchain-clang = "${HOST_PREFIX}clang-tidy"

COMPILER_RT ??= "${@bb.utils.contains("RUNTIME", "llvm", "-rtlib=compiler-rt ${UNWINDLIB}", "", d)}"
COMPILER_RT_powerpc = "--rtlib=libgcc ${UNWINDLIB}"
COMPILER_RT_armeb = "--rtlib=libgcc ${UNWINDLIB}"

UNWINDLIB ??= "${@bb.utils.contains("RUNTIME", "llvm", "--unwindlib=libgcc", "", d)}"
UNWINDLIB_riscv64 = "--unwindlib=libgcc"
UNWINDLIB_riscv32 = "--unwindlib=libgcc"
UNWINDLIB_powerpc = "--unwindlib=libgcc"
UNWINDLIB_armeb = "--unwindlib=libgcc"

LIBCPLUSPLUS ??= "${@bb.utils.contains("RUNTIME", "llvm", "--stdlib=libc++", "", d)}"

TARGET_CXXFLAGS_append_toolchain-clang = " ${LIBCPLUSPLUS}"
TUNE_CCARGS_append_toolchain-clang = " ${COMPILER_RT} ${LIBCPLUSPLUS}"

TUNE_CCARGS_remove_toolchain-clang = "-meb"
TUNE_CCARGS_remove_toolchain-clang = "-mel"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"

# Clang does not yet support big.LITTLE performance tunes, so use the LITTLE for tunes
TUNE_CCARGS_remove_toolchain-clang = "-mtune=cortex-a57.cortex-a53 -mtune=cortex-a72.cortex-a53 -mtune=cortex-a15.cortex-a7 -mtune=cortex-a17.cortex-a7 -mtune=cortex-a72.cortex-a35 -mtune=cortex-a73.cortex-a53 -mtune=cortex-a75.cortex-a55 -mtune=cortex-a76.cortex-a55"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa53 cortexa57-cortexa53 cortexa73-cortexa53", " -mtune=cortex-a53", "", d)}"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa15-cortexa7 cortexa17-cortexa7", " -mtune=cortex-a7", "", d)}"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa35", " -mtune=cortex-a35", "", d)}"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa75-cortex-a55 cortexa76-cortex-a55", " -mtune=cortex-a55", "", d)}"

# LLD does not yet support relaxation for RISCV e.g. https://reviews.freebsd.org/D25210
TUNE_CCARGS_append_toolchain-clang_riscv32 = " -mno-relax"
TUNE_CCARGS_append_toolchain-clang_riscv64 = " -mno-relax"

TUNE_CCARGS_remove_toolchain-clang_powerpc = "-mhard-float"
TUNE_CCARGS_remove_toolchain-clang_powerpc = "-mno-spe"

TUNE_CCARGS_append_toolchain-clang = " -Qunused-arguments"
TUNE_CCARGS_append_toolchain-clang_libc-musl_powerpc64 = " -mlong-double-64"
TUNE_CCARGS_append_toolchain-clang_libc-musl_powerpc64le = " -mlong-double-64"
# usrmerge workaround
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains("DISTRO_FEATURES", "usrmerge", " --dyld-prefix=/usr", "", d)}"

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
# choose between 'gnu' 'llvm'
RUNTIME ??= "gnu"
RUNTIME_toolchain-gcc = "gnu"
RUNTIME_armeb = "gnu"

TOOLCHAIN_class-native = "gcc"
TOOLCHAIN_class-nativesdk = "gcc"
TOOLCHAIN_class-cross-canadian = "gcc"
TOOLCHAIN_class-crosssdk = "gcc"
TOOLCHAIN_class-cross = "gcc"

OVERRIDES =. "${@['', 'toolchain-${TOOLCHAIN}:']['${TOOLCHAIN}' != '']}"
OVERRIDES =. "${@['', 'runtime-${RUNTIME}:']['${RUNTIME}' != '']}"
OVERRIDES[vardepsexclude] += "TOOLCHAIN RUNTIME"

#DEPENDS_append_toolchain-clang_class-target = " clang-cross-${TARGET_ARCH} "
#DEPENDS_remove_toolchain-clang_allarch = "clang-cross-${TARGET_ARCH}"

def clang_base_deps(d):
    if not d.getVar('INHIBIT_DEFAULT_DEPS', False):
        if not oe.utils.inherits(d, 'allarch') :
            ret = " clang-cross-${TARGET_ARCH} virtual/libc "
            if (d.getVar('COMPILER_RT').find('--rtlib=compiler-rt') != -1):
                ret += " compiler-rt "
            else:
                ret += " libgcc "
            if (d.getVar('COMPILER_RT').find('--unwindlib=libunwind') != -1):
                ret += " libcxx "
            if (d.getVar('LIBCPLUSPLUS').find('--stdlib=libc++') != -1):
                ret += " libcxx "
            else:
                ret += " virtual/${TARGET_PREFIX}compilerlibs "
            return ret
    return ""

BASE_DEFAULT_DEPS_toolchain-clang_class-target = "${@clang_base_deps(d)}"

cmake_do_generate_toolchain_file_append_toolchain-clang () {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
set( CMAKE_CLANG_TIDY ${CLANG_TIDY} )
EOF
    sed -i 's/ -mmusl / /g' ${WORKDIR}/toolchain.cmake
}
#
# dump recipes which still use gcc
#python __anonymous() {
#    toolchain = d.getVar("TOOLCHAIN")
#    if not toolchain or toolchain == "clang" or 'class-target' not in d.getVar('OVERRIDES').split(':'):
#        return
#    pkgn = d.getVar("PN")
#    bb.warn("%s - %s" % (pkgn, toolchain))
#}

