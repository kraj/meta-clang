LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "8934fe31f3cbfa1543b98dc101005a2142bb3fe0"
SRCREV_clang = "2785479f9a42db2c3f4e183ef1dfc026c1d40134"
SRCREV_compiler-rt = "a1df33d42314dc8c52e3eb5254f0f216397e60fc"
SRCREV_cxxabi = "89d8f36cc664fe1d590eb38175337c8293152be4"
SRCREV_libcxx = "efd039a62e635b3fc5dc68a773fbf22705c5c0e8"
SRCREV_libunwind = "8bfcea40f9973dadfaafae59192cd45b616e605b"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
