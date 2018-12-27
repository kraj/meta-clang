FILESEXTRAPATHS =. "${FILE_DIRNAME}/clang:"

LIC_FILES_CHKSUM = "file://llvm/LICENSE.TXT;md5=${LLVMMD5SUM} \
                    file://clang/LICENSE.TXT;md5=${CLANGMD5SUM} \
"
LICENSE = "NCSA"

BASEURI ??= "${LLVM_GIT}/llvm-project-20170507;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH}"
SRC_URI = "\
    ${BASEURI} \
    ${LLVMPATCHES} \
    ${CLANGPATCHES} \
    ${COMPILERRTPATCHES} \
    ${LIBCXXABIPATCHES} \
    ${LIBCXXPATCHES} \
    ${LLDBPATCHES} \
"

# llvm patches
#
LLVMPATCHES = "\
    file://0001-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch;patchdir=llvm \
    file://0002-llvm-allow-env-override-of-exe-path.patch;patchdir=llvm \
    file://0003-llvm-Disable-calls-to-_finite-and-other-glibc-only-f.patch;patchdir=llvm \
"
# Fallback to no-PIE if not set
GCCPIE ??= ""

# Clang patches
CLANGPATCHES = "\
    file://0001-clang-driver-Use-lib-for-ldso-on-OE.patch;patchdir=clang \
    file://0002-clang-Driver-tools.cpp-Add-lssp_nonshared-on-musl.patch;patchdir=clang \
    file://0003-clang-musl-ppc-does-not-support-128-bit-long-double.patch;patchdir=clang \
    file://0004-clang-Prepend-trailing-to-sysroot.patch;patchdir=clang \
    file://0005-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch;patchdir=clang \
    file://0006-clang-Define-releative-gcc-installation-dir.patch;patchdir=clang \
    file://0007-clang-Fix-ldso-for-musl-on-x86-and-x32-architectures.patch;patchdir=clang \
    file://0008-clang-scan-view-needs-python-2.x.patch;patchdir=clang \
"
CLANGPATCHES += "${@'file://0009-clang-Enable-SSP-and-PIE-by-default.patch;patchdir=clang' if '${GCCPIE}' else ''}"

# compiler-rt patches
COMPILERRTPATCHES = "\
    file://0001-compiler-rt-support-a-new-embedded-linux-target.patch;patchdir=compiler-rt \
    file://0002-compiler-rt-Simplify-cross-compilation.-Don-t-use-na.patch;patchdir=compiler-rt \
    file://0003-compiler-rt-Disable-tsan-on-OE-glibc.patch;patchdir=compiler-rt \
    file://0004-compiler-rt-cmake-mips-Do-not-specify-target-with-OE.patch;patchdir=compiler-rt \
"
# libcxxabi patches
LIBCXXABIPATCHES ="\
    file://0001-libcxxabi-Find-libunwind-headers-when-LIBCXXABI_LIBU.patch;patchdir=libcxxabi \
"

# libc++ patches
LIBCXXPATCHES = "\
"

# lldb patches
LLDBPATCHES = "\
    file://0001-lldb-Include-limits.h-for-PATH_MAX-definition.patch;patchdir=lldb \
    file://0002-lldb-Add-lxml2-to-linker-cmdline-of-xml-is-found.patch;patchdir=lldb \
"

S = "${TMPDIR}/work-shared/llvm-project-source-${PV}-${PR}/git"
B = "${WORKDIR}/llvm-project-source-${PV}/build.${HOST_SYS}.${TARGET_SYS}"

# We need to ensure that for the shared work directory, the do_patch signatures match
# The real WORKDIR location isn't a dependency for the shared workdir.
src_patches[vardepsexclude] = "WORKDIR"
should_apply[vardepsexclude] += "PN"
