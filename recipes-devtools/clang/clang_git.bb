# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
SECTION = "devel"

require clang.inc
require common-source.inc

INHIBIT_DEFAULT_DEPS = "1"

BUILD_CC:class-nativesdk = "clang"
BUILD_CXX:class-nativesdk = "clang++"
BUILD_AR:class-nativesdk = "llvm-ar"
BUILD_RANLIB:class-nativesdk = "llvm-ranlib"
BUILD_NM:class-nativesdk = "llvm-nm"
LDFLAGS:remove:class-nativesdk = "-fuse-ld=lld"

inherit cmake cmake-native pkgconfig python3native

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

def get_clang_experimental_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var)
    return ""

def get_clang_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var)
    if   re.match('(i.86|athlon|x86.64)$', a):         return 'X86'
    elif re.match('arm$', a):                          return 'ARM'
    elif re.match('armeb$', a):                        return 'ARM'
    elif re.match('aarch64$', a):                      return 'AArch64'
    elif re.match('aarch64_be$', a):                   return 'AArch64'
    elif re.match('mips(isa|)(32|64|)(r6|)(el|)$', a): return 'Mips'
    elif re.match('riscv32$', a):                      return 'riscv32'
    elif re.match('riscv64$', a):                      return 'riscv64'
    elif re.match('p(pc|owerpc)(|64)', a):             return 'PowerPC'
    else:
        bb.note("'%s' is not a primary llvm architecture" % a)
    return ""

def get_clang_host_arch(bb, d):
    return get_clang_arch(bb, d, 'HOST_ARCH')

def get_clang_target_arch(bb, d):
    return get_clang_arch(bb, d, 'TARGET_ARCH')

def get_clang_experimental_target_arch(bb, d):
    return get_clang_experimental_arch(bb, d, 'TARGET_ARCH')

PACKAGECONFIG ??= "compiler-rt libcplusplus shared-libs lldb-wchar \
                   ${@bb.utils.filter('DISTRO_FEATURES', 'thin-lto lto', d)} \
                   ${@bb.utils.contains('RUNTIME', 'llvm', 'compiler-rt libcplusplus unwindlib libomp', '', d)} \
                   rtti eh libedit terminfo \
                   "
PACKAGECONFIG:class-native = "rtti eh libedit shared-libs ${@bb.utils.contains('RUNTIME', 'llvm', 'compiler-rt libcplusplus unwindlib libomp', '', d)}"
PACKAGECONFIG:class-nativesdk = "rtti eh libedit shared-libs ${@bb.utils.filter('DISTRO_FEATURES', 'thin-lto lto', d)} ${@bb.utils.contains('RUNTIME', 'llvm', 'compiler-rt libcplusplus unwindlib libomp', '', d)}"

PACKAGECONFIG[compiler-rt] = "-DCLANG_DEFAULT_RTLIB=compiler-rt,,"
PACKAGECONFIG[libcplusplus] = "-DCLANG_DEFAULT_CXX_STDLIB=libc++,,"
PACKAGECONFIG[unwindlib] = "-DCLANG_DEFAULT_UNWINDLIB=libunwind,-DCLANG_DEFAULT_UNWINDLIB=libgcc,,"
PACKAGECONFIG[libomp] = "-DCLANG_DEFAULT_OPENMP_RUNTIME=libomp,,"
PACKAGECONFIG[thin-lto] = "-DLLVM_ENABLE_LTO=Thin -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR},,binutils,"
PACKAGECONFIG[lto] = "-DLLVM_ENABLE_LTO=Full -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR},,binutils,"
PACKAGECONFIG[shared-libs] = "-DLLVM_BUILD_LLVM_DYLIB=ON -DLLVM_LINK_LLVM_DYLIB=ON,,,"
PACKAGECONFIG[terminfo] = "-DLLVM_ENABLE_TERMINFO=ON -DCOMPILER_RT_TERMINFO_LIB=ON,-DLLVM_ENABLE_TERMINFO=OFF -DCOMPILER_RT_TERMINFO_LIB=OFF,ncurses,"
PACKAGECONFIG[pfm] = "-DLLVM_ENABLE_LIBPFM=ON,-DLLVM_ENABLE_LIBPFM=OFF,libpfm,"
PACKAGECONFIG[lldb-wchar] = "-DLLDB_EDITLINE_USE_WCHAR=1,-DLLDB_EDITLINE_USE_WCHAR=0,"
PACKAGECONFIG[lldb-lua] = "-DLLDB_ENABLE_LUA=ON,-DLLDB_ENABLE_LUA=OFF,lua"
PACKAGECONFIG[bootstrap] = "-DCLANG_ENABLE_BOOTSTRAP=On -DCLANG_BOOTSTRAP_PASSTHROUGH='${PASSTHROUGH}' -DBOOTSTRAP_LLVM_ENABLE_LTO=Thin -DBOOTSTRAP_LLVM_ENABLE_LLD=ON,,,"
PACKAGECONFIG[eh] = "-DLLVM_ENABLE_EH=ON,-DLLVM_ENABLE_EH=OFF,,"
PACKAGECONFIG[rtti] = "-DLLVM_ENABLE_RTTI=ON,-DLLVM_ENABLE_RTTI=OFF,,"
PACKAGECONFIG[split-dwarf] = "-DLLVM_USE_SPLIT_DWARF=ON,-DLLVM_USE_SPLIT_DWARF=OFF,,"
PACKAGECONFIG[libedit] = "-DLLVM_ENABLE_LIBEDIT=ON -DLLDB_ENABLE_LIBEDIT=ON,-DLLVM_ENABLE_LIBEDIT=OFF -DLLDB_ENABLE_LIBEDIT=OFF,libedit libedit-native"

OECMAKE_SOURCEPATH = "${S}/llvm"

OECMAKE_TARGET_COMPILE = "${@bb.utils.contains('PACKAGECONFIG', 'bootstrap', 'stage2', 'all', d)}"
OECMAKE_TARGET_INSTALL = "${@bb.utils.contains('PACKAGECONFIG', 'bootstrap', 'stage2-install', 'install', d)}"
BINPATHPREFIX = "${@bb.utils.contains('PACKAGECONFIG', 'bootstrap', '/tools/clang/stage2-bins/NATIVE', '', d)}"

PASSTHROUGH = "\
CLANG_DEFAULT_RTLIB;CLANG_DEFAULT_CXX_STDLIB;LLVM_BUILD_LLVM_DYLIB;LLVM_LINK_LLVM_DYLIB;\
LLVM_ENABLE_ASSERTIONS;LLVM_ENABLE_EXPENSIVE_CHECKS;LLVM_ENABLE_PIC;\
LLVM_BINDINGS_LIST;LLVM_ENABLE_FFI;FFI_INCLUDE_DIR;LLVM_OPTIMIZED_TABLEGEN;\
LLVM_ENABLE_RTTI;LLVM_ENABLE_EH;LLVM_BUILD_EXTERNAL_COMPILER_RT;CMAKE_SYSTEM_NAME;\
CMAKE_BUILD_TYPE;BUILD_SHARED_LIBS;LLVM_ENABLE_PROJECTS;LLVM_BINUTILS_INCDIR;\
LLVM_TARGETS_TO_BUILD;LLVM_EXPERIMENTAL_TARGETS_TO_BUILD;PYTHON_EXECUTABLE;\
PYTHON_LIBRARY;PYTHON_INCLUDE_DIR;LLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN;LLDB_EDITLINE_USE_WCHAR;\
LLVM_ENABLE_LIBEDIT;LLDB_ENABLE_LIBEDIT;LLDB_PYTHON_RELATIVE_PATH;LLDB_PYTHON_EXE_RELATIVE_PATH;\
LLDB_PYTHON_EXT_SUFFIX;CMAKE_C_FLAGS_RELEASE;CMAKE_CXX_FLAGS_RELEASE;CMAKE_ASM_FLAGS_RELEASE;\
CLANG_DEFAULT_CXX_STDLIB;CLANG_DEFAULT_RTLIB;CLANG_DEFAULT_UNWINDLIB;\
CLANG_DEFAULT_OPENMP_RUNTIME;\
"
#
# Default to build all OE-Core supported target arches (user overridable).
# Gennerally setting LLVM_TARGETS_TO_BUILD = "" in local.conf is ok in most simple situations
# where only one target architecture is needed along with just one build arch (usually X86)
#
LLVM_TARGETS_TO_BUILD ?= "AMDGPU;AArch64;ARM;BPF;Mips;PowerPC;RISCV;X86"

LLVM_EXPERIMENTAL_TARGETS_TO_BUILD ?= ""
LLVM_EXPERIMENTAL_TARGETS_TO_BUILD:append = ";${@get_clang_experimental_target_arch(bb, d)}"

HF = ""
HF:class-target = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"

LLVM_PROJECTS ?= "clang;clang-tools-extra;lld${LLDB}"
LLDB ?= ";lldb"
# LLDB support for RISCV/Mips32 does not work yet
LLDB:riscv32 = ""
LLDB:riscv64 = ""
LLDB:mips = ""
LLDB:mipsel = ""
LLDB:powerpc = ""

# linux hosts (.so) on Windows .pyd
SOLIBSDEV:mingw32 = ".pyd"

#CMAKE_VERBOSE = "VERBOSE=1"

EXTRA_OECMAKE += "-DLLVM_ENABLE_ASSERTIONS=OFF \
                  -DLLVM_ENABLE_EXPENSIVE_CHECKS=OFF \
                  -DLLVM_ENABLE_PIC=ON \
                  -DCLANG_DEFAULT_PIE_ON_LINUX=ON \
                  -DLLVM_BINDINGS_LIST='' \
                  -DLLVM_ENABLE_FFI=ON \
                  -DFFI_INCLUDE_DIR=$(pkg-config --variable=includedir libffi) \
                  -DLLVM_OPTIMIZED_TABLEGEN=ON \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=ON \
                  -DCMAKE_SYSTEM_NAME=Linux \
                  -DCMAKE_BUILD_TYPE=Release \
                  -DCMAKE_CXX_FLAGS_RELEASE='${CXXFLAGS} -DNDEBUG -g0' \
                  -DCMAKE_C_FLAGS_RELEASE='${CFLAGS} -DNDEBUG -g0' \
                  -DBUILD_SHARED_LIBS=OFF \
                  -DLLVM_ENABLE_PROJECTS='${LLVM_PROJECTS}' \
                  -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR} \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
                  -DLLVM_EXPERIMENTAL_TARGETS_TO_BUILD='${LLVM_EXPERIMENTAL_TARGETS_TO_BUILD}' \
"

EXTRA_OECMAKE:append:class-native = "\
                  -DPYTHON_EXECUTABLE='${PYTHON}' \
"
EXTRA_OECMAKE:append:class-nativesdk = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DCROSS_TOOLCHAIN_FLAGS_NATIVE='-DLLDB_PYTHON_RELATIVE_PATH=${PYTHON_SITEPACKAGES_DIR} \
                                                  -DLLDB_PYTHON_EXE_RELATIVE_PATH=${PYTHON} \
                                                  -DLLDB_PYTHON_EXT_SUFFIX=${SOLIBSDEV} \
                                                  -DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain-native.cmake' \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DCMAKE_STRIP=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-strip \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
                  -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
                  -DLLDB_TABLEGEN=${STAGING_BINDIR_NATIVE}/lldb-tblgen \
                  -DPYTHON_LIBRARY=${STAGING_LIBDIR}/lib${PYTHON_DIR}${PYTHON_ABI}.so \
                  -DLLDB_PYTHON_RELATIVE_PATH=${PYTHON_SITEPACKAGES_DIR} \
                  -DLLDB_PYTHON_EXE_RELATIVE_PATH=${PYTHON} \
                  -DLLDB_PYTHON_EXT_SUFFIX=${SOLIBSDEV} \
                  -DPYTHON_INCLUDE_DIR=${STAGING_INCDIR}/${PYTHON_DIR}${PYTHON_ABI} \
                  -DPYTHON_EXECUTABLE='${PYTHON}' \
"
EXTRA_OECMAKE:append:class-target = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DCROSS_TOOLCHAIN_FLAGS_NATIVE='-DLLDB_PYTHON_RELATIVE_PATH=${PYTHON_SITEPACKAGES_DIR} \
                                                  -DLLDB_PYTHON_EXT_SUFFIX=${SOLIBSDEV} \
                                                  -DLLDB_PYTHON_EXE_RELATIVE_PATH=${PYTHON}' \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
                  -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
                  -DLLDB_TABLEGEN=${STAGING_BINDIR_NATIVE}/lldb-tblgen \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DCMAKE_STRIP=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-strip \
                  -DLLVM_TARGET_ARCH=${@get_clang_target_arch(bb, d)} \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=${TARGET_SYS}${HF} \
                  -DLLVM_HOST_TRIPLE=${TARGET_SYS}${HF} \
                  -DLLDB_PYTHON_RELATIVE_PATH=${PYTHON_SITEPACKAGES_DIR} \
                  -DLLDB_PYTHON_EXE_RELATIVE_PATH=${PYTHON} \
                  -DLLDB_PYTHON_EXT_SUFFIX=${SOLIBSDEV} \
                  -DPYTHON_LIBRARY=${STAGING_LIBDIR}/lib${PYTHON_DIR}${PYTHON_ABI}.so \
                  -DPYTHON_INCLUDE_DIR=${STAGING_INCDIR}/${PYTHON_DIR}${PYTHON_ABI} \
                  -DLLVM_LIBDIR_SUFFIX=${@d.getVar('baselib').replace('lib', '')} \
                  -DPYTHON_EXECUTABLE='${PYTHON}' \
"

DEPENDS = "binutils zlib libffi libxml2 libxml2-native ninja-native swig-native"
DEPENDS:append:class-nativesdk = " clang-crosssdk-${SDK_ARCH} virtual/${TARGET_PREFIX}binutils-crosssdk nativesdk-python3"
DEPENDS:append:class-target = " clang-cross-${TARGET_ARCH} python3 compiler-rt libcxx"

RRECOMMENDS:${PN} = "binutils"
RRECOMMENDS:${PN}:append:class-target = " libcxx-dev"

do_install:append() {
    rm -rf ${D}${libdir}/python*/site-packages/six.py
}

do_install:append:class-target () {
    # Allow bin path to change based on YOCTO_ALTERNATE_EXE_PATH
    sed -i 's;${_IMPORT_PREFIX}/bin;${_IMPORT_PREFIX_BIN};g' ${D}${libdir}/cmake/llvm/LLVMExports-release.cmake

    # Insert function to populate Import Variables
    sed -i "4i\
if(DEFINED ENV{YOCTO_ALTERNATE_EXE_PATH})\n\
  execute_process(COMMAND \"llvm-config\" \"--bindir\" OUTPUT_VARIABLE _IMPORT_PREFIX_BIN OUTPUT_STRIP_TRAILING_WHITESPACE)\n\
else()\n\
  set(_IMPORT_PREFIX_BIN \"\${_IMPORT_PREFIX}/bin\")\n\
endif()\n" ${D}${libdir}/cmake/llvm/LLVMExports-release.cmake

    if [ -n "${LLVM_LIBDIR_SUFFIX}" ]; then
        mkdir -p ${D}${nonarch_libdir}
        mv ${D}${libdir}/clang ${D}${nonarch_libdir}/clang
        ln -rs ${D}${nonarch_libdir}/clang ${D}${libdir}/clang
        rmdir --ignore-fail-on-non-empty ${D}${libdir}
    fi
    for t in clang clang++ llvm-nm llvm-ar llvm-as llvm-ranlib llvm-strip llvm-objcopy llvm-objdump llvm-readelf \
        llvm-addr2line llvm-dwp llvm-size llvm-strings llvm-cov; do
        ln -sf $t ${D}${bindir}/${TARGET_PREFIX}$t
    done
}

do_install:append:class-native () {
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/lldb-tblgen ${D}${bindir}/lldb-tblgen
    for f in `find ${D}${bindir} -executable -type f -not -type l`; do
        test -n "`file -b $f|grep -i ELF`" && ${STRIP} $f
        echo "stripped $f"
    done
    ln -sf clang-tblgen ${D}${bindir}/clang-tblgen${PV}
    ln -sf llvm-tblgen ${D}${bindir}/llvm-tblgen${PV}
    ln -sf llvm-config ${D}${bindir}/llvm-config${PV}
}

do_install:append:class-nativesdk () {
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/lldb-tblgen ${D}${bindir}/lldb-tblgen
    for f in `find ${D}${bindir} -executable -type f -not -type l`; do
        test -n "`file -b $f|grep -i ELF`" && ${STRIP} $f
    done
    ln -sf clang-tblgen ${D}${bindir}/clang-tblgen${PV}
    ln -sf llvm-tblgen ${D}${bindir}/llvm-tblgen${PV}
    ln -sf llvm-config ${D}${bindir}/llvm-config${PV}
    rm -rf ${D}${datadir}/llvm/cmake
    rm -rf ${D}${datadir}/llvm
}

PACKAGES =+ "${PN}-libllvm ${PN}-lldb-python libclang lldb lldb-server liblldb"

PROVIDES += "llvm llvm${PV}"
PROVIDES:append:class-native = " llvm-native"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "\
  perl-module-digest-md5 \
  perl-module-file-basename \
  perl-module-file-copy \
  perl-module-file-find \
  perl-module-file-path \
  perl-module-findbin \
  perl-module-hash-util \
  perl-module-sys-hostname \
  perl-module-term-ansicolor \
"

RDEPENDS:lldb += "${PN}-lldb-python"

FILES:${PN}-lldb-python = "${libdir}/python*/site-packages/lldb/*"

FILES:${PN} += "\
  ${libdir}/BugpointPasses.so \
  ${libdir}/LLVMHello.so \
  ${libdir}/LLVMgold.so \
  ${libdir}/*Plugin.so \
  ${libdir}/${BPN} \
  ${nonarch_libdir}/${BPN}/*/include/ \
  ${datadir}/scan-* \
  ${nonarch_libdir}/libscanbuild \
  ${datadir}/opt-viewer/ \
"

FILES:lldb = "\
  ${bindir}/lldb \
"

FILES:lldb-server = "\
  ${bindir}/lldb-server \
"

FILES:liblldb = "\
  ${libdir}/liblldbIntelFeatures.so.* \
  ${libdir}/liblldb.so.* \
"

FILES:${PN}-libllvm =+ "\
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}git.so \
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}git.so \
"

FILES:libclang = "\
  ${libdir}/libclang.so.* \
"

FILES:${PN}-dev += "\
  ${datadir}/llvm/cmake \
  ${libdir}/cmake \
  ${nonarch_libdir}/libear \
  ${nonarch_libdir}/${BPN}/*.la \
"

FILES:${PN}-staticdev += "${nonarch_libdir}/${BPN}/*.a"

FILES:${PN}-staticdev:remove = "${libdir}/${BPN}/*.a"
FILES:${PN}-dev:remove = "${libdir}/${BPN}/*.la"
FILES:${PN}:remove = "${libdir}/${BPN}/*"


INSANE_SKIP:${PN} += "already-stripped"
#INSANE_SKIP:${PN}-dev += "dev-elf"
INSANE_SKIP:${PN}-lldb-python += "dev-so dev-deps"
INSANE_SKIP:liblldb = "dev-so"

#Avoid SSTATE_SCAN_COMMAND running sed over llvm-config.
SSTATE_SCAN_FILES:remove = "*-config"

TOOLCHAIN = "clang"
TOOLCHAIN:class-native = "gcc"
TOOLCHAIN:class-nativesdk = "clang"
COMPILER_RT:class-nativesdk:toolchain-clang:runtime-llvm = "-rtlib=libgcc --unwindlib=libgcc"
LIBCPLUSPLUS:class-nativesdk:toolchain-clang:runtime-llvm = "-stdlib=libstdc++"

SYSROOT_DIRS:append:class-target = " ${nonarch_libdir}"

SYSROOT_PREPROCESS_FUNCS:append:class-target = " clang_sysroot_preprocess"

clang_sysroot_preprocess() {
	install -d ${SYSROOT_DESTDIR}${bindir_crossscripts}/
	install -m 0755 ${S}/../llvm-config ${SYSROOT_DESTDIR}${bindir_crossscripts}/
	ln -sf llvm-config ${SYSROOT_DESTDIR}${bindir_crossscripts}/llvm-config${PV}
	# LLDTargets.cmake references the lld executable(!) that some modules/plugins link to
	install -d ${SYSROOT_DESTDIR}${bindir}
	install -m 755 ${D}${bindir}/lld ${SYSROOT_DESTDIR}${bindir}/
}
