import json
import os

MOD_ID = "tropicraft"

POTTED_PREFIX = "potted_"
BAMBOO_POTTED_PREFIX = "bamboo_potted_"

block_model_folder = "%s:block/" % MOD_ID

folders = ["blockstates", "models", "models/block"]

tropicraft_pottables = [
	"commelina_diffusa",
	"crocosmia",
	"orchid",
	"canna",
	"anemone",
	"orange_anthurium",
	"red_anthurium",
	"magic_mushroom",
	"pathos",
	"acai_vine",
	"croton",
	"dracaena",
	"fern",
	"foliage",
	"bromeliad",
	"palm_sapling",
	"mahogany_sapling",
	"grapefruit_sapling",
	"lemon_sapling",
	"lime_sapling",
	"orange_sapling"
]

def _blockstate_template(prefix, typ=""):
	t = {
		"variants": {
			"": {
				"model": "%s%s%s" % (block_model_folder, prefix, typ)
			}
		}
	}
	return t

def _block_model_template(typ="", vanilla=False):
	if vanilla:
		parent = "block/flower_pot_cross"
	else:
		parent = "tropicraft:block/bamboo_flower_pot_cross"

	t = {
	    "parent": parent,
	    "textures": {
	        "plant": "tropicraft:block/%s" % typ
	    }
	}
	return t


for folder in folders:
	if not os.path.exists(folder):
	    os.makedirs(folder)

for pottable in tropicraft_pottables:
	with open('blockstates/%s%s.json' % (BAMBOO_POTTED_PREFIX, pottable), 'w') as blockstate_file:
		json.dump(_blockstate_template(BAMBOO_POTTED_PREFIX, pottable), blockstate_file, indent=4, sort_keys=True)
	with open('models/block/%s%s.json' % (BAMBOO_POTTED_PREFIX, pottable), 'w') as blockstate_file:
		json.dump(_block_model_template(pottable, False), blockstate_file, indent=4, sort_keys=True)
	with open('blockstates/%s%s.json' % (POTTED_PREFIX, pottable), 'w') as blockstate_file:
		json.dump(_blockstate_template(POTTED_PREFIX, pottable), blockstate_file, indent=4, sort_keys=True)
	with open('models/block/%s%s.json' % (POTTED_PREFIX, pottable), 'w') as blockstate_file:
		json.dump(_block_model_template(pottable, True), blockstate_file, indent=4, sort_keys=True)
