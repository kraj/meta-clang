LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "c6901e168597f81086392c632db49ccebf4ccdc2"
SRCREV_clang = "021fcf99fa531ede83709ea6efa6dcef9e9690de"
SRCREV_compiler-rt = "9fd7ba1b347766f2668a119e949ca8c1b2923f30"
SRCREV_cxxabi = "6e0f5efa6607e6a9cd3c8999aa4141046d433fbb"
SRCREV_libcxx = "c6aee64c5d7bd5dabd5e1f5770c4f3e0388b5534"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
