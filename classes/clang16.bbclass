# Add the necessary override
CCACHE_COMPILERCHECK:toolchain-clang16 ?= "%compiler% -v"
HOST_CC_ARCH:prepend:toolchain-clang16 = "-target ${HOST_SYS} "
CC:toolchain-clang16  = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX:toolchain-clang16 = "${CCACHE}${HOST_PREFIX}clang++ ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP:toolchain-clang16 = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -E"
CCLD:toolchain-clang16 = "${CCACHE}${HOST_PREFIX}clang ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
RANLIB:toolchain-clang16 = "${HOST_PREFIX}llvm-ranlib"
AR:toolchain-clang16 = "${HOST_PREFIX}llvm-ar"
NM:toolchain-clang16 = "${HOST_PREFIX}llvm-nm"
OBJDUMP:toolchain-clang16 = "${HOST_PREFIX}llvm-objdump"
OBJCOPY:toolchain-clang16 = "${HOST_PREFIX}llvm-objcopy"
STRIP:riscv64:toolchain-clang16 = "${HOST_PREFIX}llvm-strip"
STRIP:riscv32:toolchain-clang16 = "${HOST_PREFIX}llvm-strip"
STRINGS:toolchain-clang16 = "${HOST_PREFIX}llvm-strings"
READELF:toolchain-clang16 = "${HOST_PREFIX}llvm-readelf"

LTO:toolchain-clang16 = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', '-flto=thin', '-flto -fuse-ld=lld', d)}"

COMPILER_RT ??= ""
COMPILER_RT:class-native = "-rtlib=libgcc ${UNWINDLIB}"
COMPILER_RT:armeb = "-rtlib=libgcc ${UNWINDLIB}"
COMPILER_RT:libc-klibc = "-rtlib=libgcc ${UNWINDLIB}"

UNWINDLIB ??= ""
UNWINDLIB:class-native = "--unwindlib=libgcc"
UNWINDLIB:armeb = "--unwindlib=libgcc"
UNWINDLIB_libc-klibc = "--unwindlib=libgcc"

LIBCPLUSPLUS ??= ""
LIBCPLUSPLUS:armv5 = "-stdlib=libstdc++"

CXXFLAGS:append:toolchain-clang16 = " ${LIBCPLUSPLUS}"
LDFLAGS:append:toolchain-clang16 = " ${COMPILER_RT} ${LIBCPLUSPLUS}"

TUNE_CCARGS:remove:toolchain-clang16 = "-meb"
TUNE_CCARGS:remove:toolchain-clang16 = "-mel"
TUNE_CCARGS:append:toolchain-clang16 = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", " -mbig-endian", " -mlittle-endian", d)}"

# Clang does not yet support big.LITTLE performance tunes, so use the LITTLE for tunes
TUNE_CCARGS:remove:toolchain-clang16 = "-mcpu=cortex-a57.cortex-a53 -mcpu=cortex-a72.cortex-a53 -mcpu=cortex-a15.cortex-a7 -mcpu=cortex-a17.cortex-a7 -mcpu=cortex-a72.cortex-a35 -mcpu=cortex-a73.cortex-a53 -mcpu=cortex-a75.cortex-a55 -mcpu=cortex-a76.cortex-a55"
TUNE_CCARGS:append:toolchain-clang16 = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa53 cortexa57-cortexa53 cortexa73-cortexa53", " -mcpu=cortex-a53", "", d)}"
TUNE_CCARGS:append:toolchain-clang16 = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa15-cortexa7 cortexa17-cortexa7", " -mcpu=cortex-a7", "", d)}"
TUNE_CCARGS:append:toolchain-clang16 = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa35", " -mcpu=cortex-a35", "", d)}"
TUNE_CCARGS:append:toolchain-clang16 = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa75-cortexa55 cortexa76-cortexa55", " -mcpu=cortex-a55", "", d)}"

# Workaround for https://github.com/llvm/llvm-project/issues/85699
# needed for 64bit rpi3/rpi4 machines
TUNE_CCARGS_MARCH_OPTS:append:toolchain-clang16 = "${@bb.utils.contains_any("DEFAULTTUNE", "cortexa72 cortexa53", "+nocrypto", "", d)}"

# Clang does not support octeontx2 processor
TUNE_CCARGS:remove:toolchain-clang16 = "-mcpu=octeontx2"

# Reconcile some ppc anamolies
TUNE_CCARGS:remove:toolchain-clang16:powerpc = "-mhard-float -mno-spe"
TUNE_CCARGS:append:toolchain-clang16:libc-musl:powerpc64 = " -mlong-double-64"
TUNE_CCARGS:append:toolchain-clang16:libc-musl:powerpc64le = " -mlong-double-64"
TUNE_CCARGS:append:toolchain-clang16:libc-musl:powerpc = " -mlong-double-64"
# usrmerge workaround
TUNE_CCARGS:append:toolchain-clang16 = "${@bb.utils.contains("DISTRO_FEATURES", "usrmerge", " --dyld-prefix=/usr", "", d)}"

TUNE_CCARGS:append:toolchain-clang16 = " -Qunused-arguments"

LDFLAGS:append:toolchain-clang16:class-nativesdk:x86-64 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux-x86-64.so.2"
LDFLAGS:append:toolchain-clang16:class-nativesdk:x86 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux.so.2"
LDFLAGS:append:toolchain-clang16:class-nativesdk:aarch64 = " -Wl,-dynamic-linker,${base_libdir}/ld-linux-aarch64.so.1"

LDFLAGS:toolchain-clang16:class-nativesdk = "${BUILDSDK_LDFLAGS} \
                                           -Wl,-rpath-link,${STAGING_LIBDIR}/.. \
                                           -Wl,-rpath,${libdir}/.. "

# Enable lld globally"
LDFLAGS:append:toolchain-clang16 = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-lld', ' -fuse-ld=lld', '', d)}"

# Remove gcc specific -fcanon-prefix-map option, added in gcc-13+
# clang does not support it yet
DEBUG_PREFIX_MAP:remove:toolchain-clang16 = "-fcanon-prefix-map"

# choose between 'gcc' 'clang' an empty '' can be used as well
TOOLCHAIN ??= "gcc"
# choose between 'gnu' 'llvm'
TC_CXX_RUNTIME ??= "gnu"
# Using gcc or llvm runtime is only available when using clang for compiler
#TC_CXX_RUNTIME:toolchain-gcc = "gnu"
TC_CXX_RUNTIME:armeb = "gnu"
TC_CXX_RUNTIME:armv5 = "gnu"

TOOLCHAIN:class-native = "gcc"
TOOLCHAIN:class-nativesdk = "gcc"
TOOLCHAIN:class-cross-canadian = "gcc"
TOOLCHAIN:class-crosssdk = "gcc"
TOOLCHAIN:class-cross = "gcc"

OVERRIDES =. "${@['', 'toolchain-${TOOLCHAIN}:']['${TOOLCHAIN}' != '']}"
OVERRIDES =. "${@['', 'runtime-${TC_CXX_RUNTIME}:']['${TC_CXX_RUNTIME}' != '']}"
OVERRIDES[vardepsexclude] += "TOOLCHAIN TC_CXX_RUNTIME"

YOCTO_ALTERNATE_EXE_PATH:toolchain-clang16:class-target = "${STAGING_BINDIR}/llvm-config"
YOCTO_ALTERNATE_LIBDIR:toolchain-clang16:class-target = "/${BASELIB}"

#YOCTO_ALTERNATE_EXE_PATH:toolchain-clang16:class-target[export] = "1"
#YOCTO_ALTERNATE_LIBDIR:toolchain-clang16:class-target[export] = "1"

#DEPENDS:append:toolchain-clang16:class-target = " clang16-cross-${TARGET_ARCH} "
#DEPENDS:remove:toolchain-clang16:allarch = "clang16-cross-${TARGET_ARCH}"

def clang_base_deps(d):
    if not d.getVar('INHIBIT_DEFAULT_DEPS', False):
        if not oe.utils.inherits(d, 'allarch') :
            ret = " ${MLPREFIX}clang16-cross-${TARGET_ARCH} virtual/libc "
            if (d.getVar('TC_CXX_RUNTIME').find('android') != -1):
                ret += " libcxx16-initial"
                return ret
            if (d.getVar('TC_CXX_RUNTIME').find('llvm') != -1):
                ret += " compiler-rt16"
            elif (d.getVar('COMPILER_RT').find('-rtlib=compiler-rt') != -1):
                ret += " compiler-rt16 "
            else:
                ret += " libgcc "
            if (d.getVar('TC_CXX_RUNTIME').find('llvm') != -1):
                ret += " libcxx16-initial"
            elif (d.getVar('COMPILER_RT').find('--unwindlib=libunwind') != -1):
                ret += " libcxx16-initial "
            elif (d.getVar('LIBCPLUSPLUS').find('-stdlib=libc++') != -1):
                ret += " libcxx16-initial "
            else:
                ret += " virtual/${TARGET_PREFIX}compilerlibs "
            return ret
    return ""

BASE_DEFAULT_DEPS:append:class-target:toolchain-clang16:class-target = " ${@clang_base_deps(d)}"
BASE_DEFAULT_DEPS:append:class-native:toolchain-clang16:runtime-llvm = " libcxx16-initial-native compiler-rt16-native"
BASE_DEFAULT_DEPS:append:class-nativesdk:toolchain-clang16:runtime-llvm = " clang16-native nativesdk-libcxx16-initial nativesdk-compiler-rt16"

# do_populate_sysroot needs STRIP
POPULATESYSROOTDEPS:toolchain-clang16:class-target = "${MLPREFIX}clang16-cross-${TARGET_ARCH}:do_populate_sysroot"

cmake_do_generate_toolchain_file:append:toolchain-clang16 () {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
set( CMAKE_CLANG_TIDY ${HOST_PREFIX}clang-tidy )
EOF
    sed -i 's/ -mmusl / /g' ${WORKDIR}/toolchain.cmake
}

RECIPESYSROOTFUNCS = ""
RECIPESYSROOTFUNCS:toolchain-clang16 = "recipe_sysroot_check_ld_is_lld"

recipe_sysroot_check_ld_is_lld () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-lld', 'true', 'false', d)} &&  \
        [ -e ${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}ld.lld ]; then
        ln -srf ${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}ld.lld ${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}ld
    fi
}
do_prepare_recipe_sysroot[postfuncs] += "${RECIPESYSROOTFUNCS}"
#
# dump recipes which still use gcc
#python __anonymous() {
#    toolchain = d.getVar("TOOLCHAIN")
#    if not toolchain or toolchain == "clang" or 'class-target' not in d.getVar('OVERRIDES').split(':'):
#        return
#    pkgn = d.getVar("PN")
#    bb.warn("%s - %s" % (pkgn, toolchain))
#}
