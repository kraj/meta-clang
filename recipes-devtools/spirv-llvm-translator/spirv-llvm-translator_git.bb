LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d"

BRANCH = "llvm_release_160"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-LLVM-Translator;protocol=https;branch=${BRANCH} \
           git://github.com/KhronosGroup/SPIRV-Headers;protocol=https;destsuffix=git/SPIRV-Headers;name=headers;branch=main \
          "

PV = "16.0.0+git${SRCPV}"
SRCREV = "5fdc47b1a94dbf2ad19b3a807736ddde5173e3db"
SRCREV_headers = "295cf5fb3bfe2454360e82b26bae7fc0de699abe"

SRCREV_FORMAT = "default_headers"

S = "${WORKDIR}/git"

DEPENDS = "spirv-tools clang"

inherit cmake pkgconfig python3native

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = "\
        -DBASE_LLVM_VERSION=${LLVMVERSION} \
        -DBUILD_SHARED_LIBS=ON \
        -DCMAKE_BUILD_TYPE=Release \
        -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
        -DCMAKE_SKIP_RPATH=ON \
        -DLLVM_EXTERNAL_LIT=lit \
        -DLLVM_INCLUDE_TESTS=ON \
        -Wno-dev \
        -DCCACHE_ALLOWED=FALSE \
        -DLLVM_EXTERNAL_SPIRV_HEADERS_SOURCE_DIR=${S}/SPIRV-Headers \
"

do_compile:append() {
    oe_runmake llvm-spirv
}

do_install:append() {
    install -Dm755 ${B}/tools/llvm-spirv/llvm-spirv ${D}${bindir}/llvm-spirv
}

BBCLASSEXTEND = "native nativesdk"
