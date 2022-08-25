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
           file://0001-Detect-new-BTF-api-btf_dump__new-btf_dump__new_v0_6_.patch \
           file://0001-Fix-segfault-when-btf__type_by_id-returns-NULL.patch \
"
SRCREV = "0a318e53343aa51f811183534916a4be65a1871e"

S = "${WORKDIR}/git"

inherit cmake

def llvm_major_version(d):
    pvsplit = d.getVar('LLVMVERSION').split('.')
    return pvsplit[0]

LLVM_MAJOR_VERSION = "${@llvm_major_version(d)}"

EXTRA_OECMAKE = " \
    -DCMAKE_ENABLE_EXPORTS=1 \
    -DCMAKE_BUILD_TYPE=Release \
    -DLLVM_REQUESTED_VERSION=${LLVM_MAJOR_VERSION} \
    -DBUILD_TESTING=OFF \
    -DENABLE_MAN=OFF \
"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*|riscv64.*)-linux"
COMPATIBLE_HOST:libc-musl = "null"
