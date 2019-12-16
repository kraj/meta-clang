LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm"
LLVM_GIT_PROTOCOL ?= "https"

MAJOR_VER = "9"
MINOR_VER = "0"
PATCH_VER = "1"

SRCREV ?= "432bf48c08da748e2542cf40e3ab9aee53a744b0"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release/${MAJOR_VER}.x"

LLVMMD5SUM = "8a15a0759ef07f2682d2ba4b893c9afe"
CLANGMD5SUM = "ff42885ed2ab98f1ecb8c1fc41205343"
LLDMD5SUM = "ae7dc7c027b1fa89b5b013d391d3ee2b"
LLDBMD5SUM = "2e0d44968471fcde980034dbb826bea9"

def get_libdir_suffix(d, arch_var):
    import re
    multilibs = (d.getVar("MULTILIB_VARIANTS") or "").split()
    if multilibs:
        a = d.getVar(arch_var, True)
        if   re.match('(i.86|athlon)$', a):          return '32'
        elif re.match('x86.64$', a):                 return '64'
        elif re.match('(arm|armbe)$', a):            return '32'
        elif re.match('(aarch64|aarch64_be)$', a):   return '64'
        elif re.match('mips(isa|)32(r6|)(el|)$', a): return '32'
        elif re.match('mips(isa|)64(r6|)(el|)$', a): return '64'
        elif re.match('p(pc|owerpc)', a):            return '32'
        elif re.match('p(pc|owerpc)64', a):          return '64'
    else:
        return ''

LLVM_LIBDIR_SUFFIX="${@get_libdir_suffix(d, 'TARGET_ARCH')}"

require common.inc
