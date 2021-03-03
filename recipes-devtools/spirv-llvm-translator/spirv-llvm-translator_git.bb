LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d"

BRANCH = "llvm_release_120"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-LLVM-Translator/;protocol=https;branch=${BRANCH} \
           file://0001-Use-12.0.0-for-base-llvm-version.patch \
           file://0001-cmake-allow-to-enable-disable-ccache.patch \
          "

PV = "12.0.0"
SRCREV = "c65388fcd4eac9ce070fc330d5fe263ba2c72d66"

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
"

do_compile_append() {
    oe_runmake llvm-spirv
}

do_install_append() {
    install -Dm755 ${B}/tools/llvm-spirv/llvm-spirv ${D}${bindir}/llvm-spirv
}

BBCLASSEXTEND = "native nativesdk"
