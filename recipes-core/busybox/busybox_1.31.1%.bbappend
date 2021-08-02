FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:toolchain-clang = "\
    file://0001-Turn-ptr_to_globals-and-bb_errno-to-be-non-const-1.31.1.patch \
"

# networking/tls_pstm_sqr_comba.c:514:4: error: inline assembly requires more registers than available
#                        SQRADD2(*tmpx++, *tmpy--);
#                        ^
TOOLCHAIN:x86 = "gcc"
