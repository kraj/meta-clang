LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d"

BRANCH = "main"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-LLVM-Translator;protocol=https;branch=${BRANCH} \
           git://github.com/KhronosGroup/SPIRV-Headers;protocol=https;destsuffix=git/SPIRV-Headers;name=headers;branch=master \
          "

PV = "15.0.0"
SRCREV = "1b8a00741caafac50de84f1f860b78e702722585"
SRCREV_headers = "0bcc624926a25a2a273d07877fd25a6ff5ba1cfb"

SRCREV_FORMAT = "default_headers"

S = "${WORKDIR}/git"

DEPENDS = "spirv-tools clang"

inherit cmake pkgconfig python3native

OECMAKE_GENERATOR = "Unix Makefiles"

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = "\
        -DBUILD_SHARED_LIBS=ON \
        -DLLVM_SPIRV_BUILD_EXTERNAL=YES \
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
