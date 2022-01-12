# networking/tls_pstm_sqr_comba.c:514:4: error: inline assembly requires more registers than available
#                        SQRADD2(*tmpx++, *tmpy--);
#                        ^
TOOLCHAIN:x86 = "gcc"

# Disable the 'const trick' on clang (see busybox 1f925038a)
CFLAGS:append:toolchain-clang = " -DBB_GLOBAL_CONST=''"
