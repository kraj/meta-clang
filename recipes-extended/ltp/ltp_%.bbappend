# Old versions of ltp assume the compiler defaults to -fcommon
# This can be removed when moving to a newer version of ltp.
# The fix is also needed when building with gcc >= 10.
CFLAGS_append = " -fcommon"
