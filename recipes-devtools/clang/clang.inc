LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "30623ab7a75cddce76977215a860687c26fc2526"
SRCREV_clang = "7759c7d28c3c5ad00d2e3310384c5e7073de1f97"
SRCREV_compiler-rt = "1b97e77b4757a3b1ee187eb5ebcac57ab1d1717f"
SRCREV_cxxabi = "534295b102a7f9951ab2b8b38637b9cdf57cade1"
SRCREV_libcxx = "c480bc55cdcdb884d24fbe96cc0c29da44939186"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
