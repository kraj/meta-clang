FILESEXTRAPATHS:prepend := "${THISDIR}/libbpf:"

LIC_FILES_CHKSUM = "file://LICENSE.LGPL-2.1;md5=b370887980db5dd40659b50909238dbd"

SRC_URI += "\
            file://0001-libbpf-Introduce-bpf_-btf-link-map-prog-_get_info_by.patch \
            file://0001-bpf-Add-multi-kprobe-link.patch \
            file://0001-libbpf-Add-bpf_link_create-support-for-multi-kprobes.patch \
            file://0001-bpf-Add-btf-enum64-support.patch \
            file://0001-bpf-Add-cookie-support-to-programs-attached-with-kpr.patch \
            file://0001-libbpf-Add-enum64-sanitization.patch \
            file://0001-libbpf-Refactor-btf__add_enum-for-future-code-sharin.patch \
            file://0001-libbpf-Add-enum64-parsing-and-new-enum64-public-API.patch"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += " -C ${S}/src"
