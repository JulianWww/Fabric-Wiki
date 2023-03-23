from wget import download
from os import mkdir
import numpy as np
import cv2
import mc_generator_utils as mc

tmpDir = ".tmp"
try: mkdir(tmpDir)
except FileExistsError: pass

filePath = download("https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/gui/resource_packs.png", out=tmpDir)
guiPath = mc.assets("wiki") + "/textures/gui/"
thisPath = "generators/textures/"

## Generate wiki selection buttons

img = cv2.imread(filePath, cv2.IMREAD_UNCHANGED)

def generateButtonGroup(img, x):
  out = np.zeros((32, 64, img.shape[2]), dtype=np.int16)

  orig = img[32*(x): 32*(x+1), :32, :]

  out[:32, :32] = orig
  out[:, 32:, :] = np.swapaxes(orig,0,1)
  return out


buttons = 2
out = []
for idx in range(buttons):
  out.append(generateButtonGroup(img, idx))

cv2.imwrite(guiPath + "pageselection.png", np.concatenate(out, 0))

## Generate Small Buttons
buttons = ["home"]
base = mc.load(thisPath + "data/small_button.png")

out = []
for button in buttons:
  mask = mc.load(f"{thisPath}data/{button}.png")
  img = np.copy(base)
  img = mc.stackImages(img, mask, 0, 0)
  img = mc.stackImages(img, mask, 20, 0)
  out.append(img)

cv2.imwrite(guiPath + "small_buttons.png", np.concatenate(out, axis=1))
