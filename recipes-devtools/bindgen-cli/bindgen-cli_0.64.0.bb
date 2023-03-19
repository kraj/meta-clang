SUMMARY = "Automatically generates Rust FFI bindings to C and C++ libraries."
HOMEPAGE = "https://rust-lang.github.io/rust-bindgen/"
LICENSE = "BSD-3-Clause"

LIC_FILES_CHKSUM = "file://LICENSE;md5=0b9a98cb3dcdefcceb145324693fda9b"

inherit rust cargo cargo-update-recipe-crates

SRC_URI = "crate://crates.io/${BPN}/${PV}"
SRC_URI[bindgen-cli.sha256sum] = "ae0d083ff9e4484d9a6a3f8e39bfc08f984e69c5981896123518b9a1e31d3307"

require ${BPN}-crates.inc

do_install:append:class-native() {
	create_wrapper ${D}/${bindir}/bindgen LIBCLANG_PATH="${STAGING_LIBDIR_NATIVE}"
}

RDEPENDS:${PN} = "libclang"

BBCLASSEXTEND = "native"
