#!/usr/bin/env python3
# bHold icon: hold-progress ring around a set block, red on the family
# dark bg. Deliberately no grid motif. Outputs 512/128/16 px.
from PIL import Image, ImageDraw

BG = (38, 40, 48)
RED = (205, 95, 95)
DRED = (150, 60, 60)
LRED = (235, 150, 150)


def out(im, name):
    im.save('bholdeasyplace-icon-512.png')
    im.resize((128, 128), Image.LANCZOS).save('bholdeasyplace-icon-128.png')
    im.resize((16, 16), Image.LANCZOS).save('bholdeasyplace-icon-16.png')


# B: hold ring. 270-degree progress arc around a solid centre block.
im = Image.new('RGB', (512, 512), BG)
d = ImageDraw.Draw(im)
d.arc([72, 72, 440, 440], start=-90, end=180, fill=RED, width=52)
d.rounded_rectangle([180, 180, 332, 332], radius=24, fill=RED)
d.rounded_rectangle([216, 216, 296, 296], radius=12, fill=LRED)
out(im, 'B')


