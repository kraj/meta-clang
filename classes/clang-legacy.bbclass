# Add the necessary override
LTO:toolchain-clang:class-target = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', '-flto=thin', '-flto -fuse-ld=lld', d)}"
LTO:toolchain-clang:class-nativesdk = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', '-flto=thin', '-flto -fuse-ld=lld', d)}"

COMPILER_RT:toolchain-clang:armeb = "-rtlib=libgcc ${UNWINDLIB}"
UNWINDLIB:toolchain-clang:armeb = "--unwindlib=libgcc"
LIBCPLUSPLUS::toolchain-clang:armv5 = "-stdlib=libstdc++"

# Clang does not support octeontx2 processor
TUNE_CCARGS_MARCH_OPTS ??= ""
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
