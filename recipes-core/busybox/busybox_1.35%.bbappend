FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang = "\
                                  file://0001-apply-const-trick-to-ptr_to_globals.patch \
                                  file://clang.cfg"

# networking/tls_pstm_sqr_comba.c:514:4: error: inline assembly requires more registers than available
#                        SQRADD2(*tmpx++, *tmpy--);
#                        ^
TOOLCHAIN:x86 = "gcc"
