FILESEXTRAPATHS =. "${FILE_DIRNAME}/clang:"

# llvm patches
#file://0001-llvm-Remove-CMAKE_CROSSCOMPILING-so-it-can-cross-com.patch \
#
LLVMPATCHES = "\
    file://0001-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
    file://0002-llvm-allow-env-override-of-exe-path.patch \
"
# Fallback to no-PIE if not set
GCCPIE ??= ""

# Clang patches
CLANGPATCHES = "\
    file://0001-clang-driver-Use-lib-for-ldso-on-OE.patch;patchdir=tools/clang \
    file://0002-clang-Driver-tools.cpp-Add-lssp-and-lssp_nonshared-o.patch;patchdir=tools/clang \
    file://0003-clang-musl-ppc-does-not-support-128-bit-long-double.patch;patchdir=tools/clang \
    file://0004-clang-Prepend-trailing-to-sysroot.patch;patchdir=tools/clang \
    file://0005-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch;patchdir=tools/clang \
    file://0006-clang-Define-releative-gcc-installation-dir.patch;patchdir=tools/clang \
"
CLANGPATCHES += "${@'file://0007-clang-Enable-SSP-and-PIE-by-default.patch;patchdir=tools/clang' if '${GCCPIE}' else ''}"

# libcxxabi patches
LIBCXXABIPATCHES ="\
    file://0001-libcxxabi-Fix-arm-EHABI-code-to-work.-armhf-had-exce.patch;patchdir=projects/libcxxabi \
"

# libc++ patches
LIBCXXPATCHES = "\
    file://0002-libcxx-Do-not-include-xlocale.h-on-glibc.patch;patchdir=projects/libcxx \
"

LIBCXXPATCHES_append_libc-musl = "\
    file://0001-libcxx-use-constexpr-when-using-glibc.patch;patchdir=projects/libcxx \
"

