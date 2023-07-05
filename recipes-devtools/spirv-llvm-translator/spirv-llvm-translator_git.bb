LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d"

BRANCH = "main"
SRC_URI = "git://github.com/KhronosGroup/SPIRV-LLVM-Translator;protocol=https;branch=${BRANCH} \
           git://github.com/KhronosGroup/SPIRV-Headers;protocol=https;destsuffix=git/SPIRV-Headers;name=headers;branch=main \
          "

PV = "17.0.0+git${SRCPV}"
SRCREV = "574b0c6d0368bdd5f1147ac1a01d75718462e3ec"
SRCREV_headers = "d0006a3938d7acedffb26ab517fe3e95b5288cc6"

SRCREV_FORMAT = "default_headers"

S = "${WORKDIR}/git"

DEPENDS = "spirv-tools clang"

inherit cmake pkgconfig python3native

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
# for CMAKE_SHARED_LIBS=OFF see https://github.com/KhronosGroup/SPIRV-LLVM-Translator/issues/1868
EXTRA_OECMAKE = "\
        -DBASE_LLVM_VERSION=${LLVMVERSION} \
        -DBUILD_SHARED_LIBS=OFF \
        -DCMAKE_BUILD_TYPE=Release \
        -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
        -DCMAKE_SKIP_RPATH=ON \
        -DLLVM_EXTERNAL_LIT=lit \
        -DLLVM_INCLUDE_TESTS=ON \
        -Wno-dev \
        -DCCACHE_ALLOWED=FALSE \
        -DLLVM_EXTERNAL_SPIRV_HEADERS_SOURCE_DIR=${S}/SPIRV-Headers \
"

BBCLASSEXTEND = "native nativesdk"
