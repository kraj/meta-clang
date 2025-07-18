# Add the necessary override
CCACHE_COMPILERCHECK:toolchain-clang ?= "%compiler% -v"

LTO:toolchain-clang:class-target = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', '-flto=thin', '-flto -fuse-ld=lld', d)}"
LTO:toolchain-clang:class-nativesdk = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', '-flto=thin', '-flto -fuse-ld=lld', d)}"

COMPILER_RT:toolchain-clang:armeb = "-rtlib=libgcc ${UNWINDLIB}"
COMPILER_RT:toolchain-clang:libc-klibc = "-rtlib=libgcc ${UNWINDLIB}"

UNWINDLIB:toolchain-clang:armeb = "--unwindlib=libgcc"
UNWINDLIB:toolchain-clang:libc-klibc = "--unwindlib=libgcc"

LIBCPLUSPLUS::toolchain-clang:armv5 = "-stdlib=libstdc++"

# Clang does not yet support big.LITTLE performance tunes, so use the LITTLE for tunes
TUNE_CCARGS:remove:toolchain-clang = "\
    -mcpu=cortex-a57.cortex-a53${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a72.cortex-a53${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a15.cortex-a7${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a17.cortex-a7${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a72.cortex-a35${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a73.cortex-a53${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a75.cortex-a55${TUNE_CCARGS_MARCH_OPTS} \
    -mcpu=cortex-a76.cortex-a55${TUNE_CCARGS_MARCH_OPTS}"
TUNE_CCARGS:append:toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa53 cortexa57-cortexa53 cortexa73-cortexa53", " -mcpu=cortex-a53${TUNE_CCARGS_MARCH_OPTS}", "", d)}"
TUNE_CCARGS:append:toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa15-cortexa7 cortexa17-cortexa7", " -mcpu=cortex-a7${TUNE_CCARGS_MARCH_OPTS}", "", d)}"
TUNE_CCARGS:append:toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa35", " -mcpu=cortex-a35${TUNE_CCARGS_MARCH_OPTS}", "", d)}"
TUNE_CCARGS:append:toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa75-cortexa55 cortexa76-cortexa55", " -mcpu=cortex-a55${TUNE_CCARGS_MARCH_OPTS}", "", d)}"

# Workaround for https://github.com/llvm/llvm-project/issues/85699
# needed for 64bit rpi3/rpi4 machines
TUNE_CCARGS_MARCH_OPTS:append:toolchain-clang = "${@bb.utils.contains_any("DEFAULTTUNE", "cortexa72 cortexa53", "+nocrypto", "", d)}"

# Clang does not support octeontx2 processor
TUNE_CCARGS:remove:toolchain-clang = "-mcpu=octeontx2${TUNE_CCARGS_MARCH_OPTS}"

# Reconcile some ppc anamolies
TUNE_CCARGS:remove:toolchain-clang:powerpc = "-mhard-float -mno-spe"
TUNE_CCARGS:append:toolchain-clang:libc-musl:powerpc64 = " -mlong-double-64"
TUNE_CCARGS:append:toolchain-clang:libc-musl:powerpc64le = " -mlong-double-64"
TUNE_CCARGS:append:toolchain-clang:libc-musl:powerpc = " -mlong-double-64"

# If lld is enabled globally then disable it for ppc32 where it causes random segfaults in Qemu usermode
LDFLAGS:remove:toolchain-clang:powerpc = "-fuse-ld=lld"
LDFLAGS:append:toolchain-clang:powerpc = " -fuse-ld=bfd"

# Using gcc or llvm runtime is only available when using clang for compiler
#TC_CXX_RUNTIME:toolchain-gcc = "gnu"
TC_CXX_RUNTIME:armeb = "gnu"
TC_CXX_RUNTIME:armv5 = "gnu"

#TOOLCHAIN:class-native = "gcc"
#TOOLCHAIN:class-nativesdk = "gcc"
#TOOLCHAIN:class-cross-canadian = "gcc"
#TOOLCHAIN:class-crosssdk = "gcc"
#TOOLCHAIN:class-cross = "gcc"

#OVERRIDES =. "${@['', 'toolchain-${TOOLCHAIN}:']['${TOOLCHAIN}' != '']}"
OVERRIDES =. "${@['', 'runtime-${TC_CXX_RUNTIME}:']['${TC_CXX_RUNTIME}' != '']}"
OVERRIDES[vardepsexclude] += "TC_CXX_RUNTIME"

YOCTO_ALTERNATE_EXE_PATH:toolchain-clang:class-target = "${STAGING_BINDIR}/llvm-config"
YOCTO_ALTERNATE_LIBDIR:toolchain-clang:class-target = "/${BASELIB}"

#YOCTO_ALTERNATE_EXE_PATH:toolchain-clang:class-target[export] = "1"
#YOCTO_ALTERNATE_LIBDIR:toolchain-clang:class-target[export] = "1"

#DEPENDS:append:toolchain-clang:class-target = " clang-cross-${TARGET_ARCH} "
#DEPENDS:remove:toolchain-clang:allarch = "clang-cross-${TARGET_ARCH}"

# dump recipes which still use gcc
#python __anonymous() {
#    toolchain = d.getVar("TOOLCHAIN")
#    if not toolchain or toolchain == "clang" or 'class-target' not in d.getVar('OVERRIDES').split(':'):
#        return
#    pkgn = d.getVar("PN")
#    bb.warn("%s - %s" % (pkgn, toolchain))
#}
