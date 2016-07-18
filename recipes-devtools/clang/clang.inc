LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "c9ba7aa68b755a0b749345d740e6f3eae24e5a6c"
SRCREV_clang = "56d5bb93dee4e1dcc29ee41fe2caf9ffed9094d2"
SRCREV_compiler-rt = "72a44e79ce4b46c9b6a5eb588d0378e3dc0e631b"
SRCREV_cxxabi = "e8b3ec33ccb6f76caea7388317d5620b9c36de51"
SRCREV_libcxx = "066feb82496556cbac51cb1d1d4f18329e37fec5"
SRCREV_libunwind = "878c40b2f240ee9f57be7dce86caf7650f79dc7f"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
