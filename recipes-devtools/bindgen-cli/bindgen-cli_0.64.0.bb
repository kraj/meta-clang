SUMMARY = "Automatically generates Rust FFI bindings to C and C++ libraries."
HOMEPAGE = "https://rust-lang.github.io/rust-bindgen/"
LICENSE = "BSD-3-Clause"

LIC_FILES_CHKSUM = "file://bindgen-cli/LICENSE;md5=0b9a98cb3dcdefcceb145324693fda9b"

inherit rust cargo cargo-update-recipe-crates

SRC_URI = "git://github.com/rust-lang/rust-bindgen.git;protocol=https;nobranch=1;branch=main"
SRCREV = "ae6817256ac557981906e93a1f866349db85053e"

S = "${WORKDIR}/git"

CARGO_SRC_DIR = "bindgen-cli"
CARGO_LOCK_SRC_DIR = "bindgen-cli"

do_install:append:class-native() {
	create_wrapper ${D}/${bindir}/bindgen LIBCLANG_PATH="${STAGING_LIBDIR_NATIVE}"
}

RDEPENDS:${PN} = "libclang"

BBCLASSEXTEND = "native"
