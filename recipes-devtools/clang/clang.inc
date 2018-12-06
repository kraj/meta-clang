LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "https"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER = "7"
MINOR_VER = "0"
PATCH_VER = "1"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_${MAJOR_VER}${MINOR_VER}"

SRCREV_llvm = "3315ebed51e7e5e3fc01578455242e9d5f4fdb11"
SRCREV_clang = "b540402b6b6657877032791cfc6df4eb87b39318"
SRCREV_compiler-rt = "a4cbb02bca3b952117e9ccfbad8a485857f25935"
SRCREV_cxxabi = "a5b1f6f7644b30c97a59d512d4dbe6825db424d9"
SRCREV_libcxx = "2ac8add7b38ccbbdf75399caf84aeabaf687da29"
SRCREV_libunwind = "d1dee9d525dac7d7146b6e197bc330a7bd963075"
SRCREV_lld = "b9f34e3a65782a9f33fe9eaf2240ec4f1f6e3f6e"
SRCREV_lldb = "637da661b5ef6fd47f4b077ffd26a79b1c1892f9"
SRCREV_openmp = "366ce74b85790ed41f94fba7f17a0911bde83500"

LLVMMD5SUM = "c520ed40e11887bb1d24d86f7f5b1f05"
CLANGMD5SUM = "444af0e124949f07f791f12c928e5994"
LLDMD5SUM = "f4941ace8ddb3d6cf177fff94966319a"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"

COMPATIBLE_HOST_riscv64 = "(null)"
