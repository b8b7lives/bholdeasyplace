#!/usr/bin/env python3
# bHold banner: family layout (motif left, pixel wordmark, gray tag).
# Low-res render upscaled nearest for the chunky look. Outputs 2560x1280
# master and 1280x640 social size.
from PIL import Image, ImageDraw, ImageFont

BG = (38, 40, 48)
RED = (205, 95, 95)
LRED = (235, 150, 150)
FG = (232, 234, 237)
GRAY = (150, 155, 165)

NAME = 'bHold easyPlace'
TAG = 'a b8b7 mod'
FONT = '/usr/share/fonts/truetype/dejavu/DejaVuSansMono-Bold.ttf'

im = Image.new('RGB', (320, 160), BG)
d = ImageDraw.Draw(im)
# bilevel text: hard pixel edges survive the nearest upscale
d.fontmode = '1'

d.arc([22, 56, 70, 104], start=-90, end=180, fill=RED, width=7)
d.rounded_rectangle([36, 70, 56, 90], radius=3, fill=RED)
d.rounded_rectangle([41, 75, 51, 85], radius=2, fill=LRED)

name_font = ImageFont.truetype(FONT, 20)
tag_font = ImageFont.truetype(FONT, 9)
d.text((86, 62), NAME, font=name_font, fill=FG)
d.text((87, 92), TAG, font=tag_font, fill=GRAY)

big = im.resize((2560, 1280), Image.NEAREST)
big.save('banner.png')
big.resize((1280, 640), Image.LANCZOS).save('banner-social-1280x640.png')
print('banner 2560x1280 + social 1280x640')
