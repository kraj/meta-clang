SUMMARY = "Automatically generates Rust FFI bindings to C and C++ libraries."
HOMEPAGE = "https://rust-lang.github.io/rust-bindgen/"
LICENSE = "BSD-3-Clause"

LIC_FILES_CHKSUM = "file://LICENSE;md5=0b9a98cb3dcdefcceb145324693fda9b"

inherit rust cargo cargo-update-recipe-crates

SRCREV = "ae6817256ac557981906e93a1f866349db85053e"
SRC_URI = "git://github.com/rust-lang/rust-bindgen;protocol=https;branch=main"

S = "${WORKDIR}/git"

require ${BPN}-crates.inc

do_install:append:class-native() {
	create_wrapper ${D}/${bindir}/bindgen LIBCLANG_PATH="${STAGING_LIBDIR_NATIVE}"
}

RDEPENDS:${PN} = "libclang"

BBCLASSEXTEND = "native"
