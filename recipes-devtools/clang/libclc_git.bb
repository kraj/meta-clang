SUMMARY = "Implementation of the library requirements of the OpenCL 1.1+ C programming language"
HOMEPAGE = "http://libclc.llvm.org/"
SECTION = "libs"

require clang.inc
require common-source.inc

BPN = "libclc"

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://libclc/LICENSE.TXT;md5=7cc795f6cbb2d801d84336b83c8017db"

inherit cmake cmake-qemu pkgconfig python3native

DEPENDS += "clang spirv-tools spirv-llvm-translator spirv-llvm-translator-native ncurses"

OECMAKE_SOURCEPATH = "${S}/libclc"

EXTRA_OECMAKE += "\
    -DLLVM_APPEND_VC_REV=OFF \
    -DLIBCLC_CUSTOM_LLVM_TOOLS_BINARY_DIR=${STAGING_BINDIR_NATIVE} \
    -DLLVM_CLANG=${STAGING_BINDIR_NATIVE}/clang \
    -DLLVM_AS=${STAGING_BINDIR_NATIVE}/llvm-as \
    -DLLVM_LINK=${STAGING_BINDIR_NATIVE}/llvm-link \
    -DLLVM_OPT=${STAGING_BINDIR_NATIVE}/opt \
    -DLLVM_SPIRV=${STAGING_BINDIR_NATIVE}/llvm-spirv \
    -DCMAKE_BUILD_TYPE=RelWithDebInfo \
    -Dclc_comp_in:FILEPATH=${OECMAKE_SOURCEPATH}/cmake/CMakeCLCCompiler.cmake.in \
    -Dll_comp_in:FILEPATH=${OECMAKE_SOURCEPATH}/cmake/CMakeLLAsmCompiler.cmake.in \
    -DCMAKE_POSITION_INDEPENDENT_CODE=ON"

FILES:${PN} += "${datadir}/clc"

BBCLASSEXTEND = "native nativesdk"

export YOCTO_ALTERNATE_EXE_PATH
export YOCTO_ALTERNATE_LIBDIR
