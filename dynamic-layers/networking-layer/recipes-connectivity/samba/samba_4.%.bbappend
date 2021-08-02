FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://fix_duplicate_pdb_search_init.patch "
