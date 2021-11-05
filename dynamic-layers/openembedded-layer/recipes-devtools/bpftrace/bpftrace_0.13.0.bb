SUMMARY = "bpftrace"
HOMEPAGE = "https://github.com/iovisor/bpftrace"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "bison-native \
            flex-native \
            gzip-native \
            elfutils \
            bcc \
            systemtap \
            libcereal \
            libbpf \
            "

PV .= "+git${SRCREV}"
RDEPENDS:${PN} += "bash python3 xz"

SRC_URI = "git://github.com/iovisor/bpftrace;branch=master;protocol=https \
           file://0001-support-clang-upto-version-13.patch \
           file://0002-orc-Fix-build-with-clang-13.patch \
           "
SRCREV = "9b929db174be11888f60d16b1b090e4603bc3a51"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = " \
    -DCMAKE_ENABLE_EXPORTS=1 \
    -DCMAKE_BUILD_TYPE=Release \
    -DLLVM_REQUESTED_VERSION=13 \
    -DBUILD_TESTING=OFF \
    -DENABLE_MAN=OFF \
"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
COMPATIBLE_HOST:libc-musl = "null"
