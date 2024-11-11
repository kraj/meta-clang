LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d"

BRANCH = "llvm_release_140"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-LLVM-Translator;protocol=https;branch=${BRANCH} \
           git://github.com/KhronosGroup/SPIRV-Headers;protocol=https;destsuffix=git/SPIRV-Headers;name=headers;branch=main \
          "

PV = "14.0.0"
SRCREV = "8937ecf4698c9283c92c35b65e5b97e6e0acf869"
SRCREV_headers = "b42ba6d92faf6b4938e6f22ddd186dbdacc98d78"

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
