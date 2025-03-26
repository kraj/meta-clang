FILESEXTRAPATHS:prepend := "${THISDIR}/libbpf:"

LIC_FILES_CHKSUM = "file://LICENSE.LGPL-2.1;md5=b370887980db5dd40659b50909238dbd"

SRC_URI += "\
            file://0001-libbpf-Introduce-bpf_-btf-link-map-prog-_get_info_by.patch"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += " -C ${S}/src"
