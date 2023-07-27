FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang = "\
    file://0001-Removed-unused-variable-supp_size-from-plan_subset_e.patch \
"
