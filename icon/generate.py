#!/usr/bin/env python3
# bHold icon drafts: "hold" motif, red accent on the family dark bg.
# Deliberately no grid motif (punchlist/rendermatica territory).
# Outputs drafts A/B/C at 512 plus 128/16 previews.
from PIL import Image, ImageDraw

BG = (38, 40, 48)
RED = (205, 95, 95)
DRED = (150, 60, 60)
LRED = (235, 150, 150)


def out(im, name):
    im.save(f'draft-{name}-512.png')
    im.resize((128, 128), Image.LANCZOS).save(f'draft-{name}-128.png')
    im.resize((16, 16), Image.LANCZOS).save(f'draft-{name}-16.png')


# A: pressed keycap. Cap sits low-right of its shadow ledge, reads "held down".
im = Image.new('RGB', (512, 512), BG)
d = ImageDraw.Draw(im)
d.rounded_rectangle([96, 128, 416, 448], radius=48, fill=DRED)
d.rounded_rectangle([120, 96, 440, 416], radius=48, fill=RED)
d.rounded_rectangle([168, 144, 392, 344], radius=32, fill=LRED)
out(im, 'A')

# B: hold ring. 270-degree progress arc around a solid centre block.
im = Image.new('RGB', (512, 512), BG)
d = ImageDraw.Draw(im)
d.arc([72, 72, 440, 440], start=-90, end=180, fill=RED, width=52)
d.rounded_rectangle([180, 180, 332, 332], radius=24, fill=RED)
d.rounded_rectangle([216, 216, 296, 296], radius=12, fill=LRED)
out(im, 'B')

# C: right mouse button held. Mouse body dim, RMB lit, click arcs.
im = Image.new('RGB', (512, 512), BG)
d = ImageDraw.Draw(im)
d.rounded_rectangle([144, 96, 368, 448], radius=100, fill=DRED)
d.rounded_rectangle([160, 112, 352, 432], radius=88, fill=BG)
d.rounded_rectangle([176, 128, 336, 416], radius=76, fill=DRED)
d.pieslice([176, 128, 336, 288], start=270, end=360, fill=RED)
d.line([256, 128, 256, 240], fill=BG, width=12)
d.arc([320, 96, 448, 224], start=-60, end=40, fill=RED, width=24)
d.arc([352, 64, 496, 208], start=-60, end=40, fill=LRED, width=20)
out(im, 'C')

print('drafts A/B/C at 512/128/16')
