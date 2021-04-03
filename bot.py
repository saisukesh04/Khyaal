import discord
from discord.ext import commands 
import random 
import requests
from datetime import date 


import keys


client = commands.Bot(command_prefix= '.')



@client.event
async def on_ready():
    print("Khyaal Bot is ready")


@client.command()
async def ping(ctx):
    await ctx.send(f'Pong! Latency = {client.latency * 1000} ms')

@client.command()
"""
Helper function to see if the bot is working, and to send a plaintext message
"""
async def hi(ctx):
    await ctx.send(f'Hi!')

@client.command()
async def test(ctx, arg1, arg2):
    url1 = 'https://api.edamam.com/api/food-database/v2/parser?ingr={}&app_id={}&app_key={}'.format(arg1,keys.ID,keys.APP_KEY)
    url2 = 'https://api.edamam.com/api/food-database/v2/parser?ingr={}&app_id={}&app_key={}'.format(arg2,keys.ID,keys.APP_KEY)
    print(requests.get(url1).status_code)
    print(requests.get(url2).text)
    x = requests.get(url2).json()
    z = requests.get(url1).json()
    y = x['parsed'][0]['food']['nutrients']
    k = z['parsed'][0]['food']['nutrients']
    await ctx.send(f'{y},\n {k}')
    
@client.command()
"""
Helper function to enbed an image
"""
async def randomimage(ctx):
   e = discord.Embed(title="Your title here", description="Your desc here")
   e.set_image(url="https://i.imgur.com/SJgskbM.jpg")
   # file = discord.File("https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=32&w=32")
   await ctx.send("Random Image",embed=e)
   
@client.command()
async def getrecipes(ctx, listOfIng):
    url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients={}&number=3&apiKey={}).format(listOfIng,numRecipes,keys.spoonacular_api_key)"
    r = requests.get(url)
    print(r.status_code)
    x = r.json()
    for i in range(3):
        print(x[i])
        e = discord.Embed(title=x[i]['title'],
        description = "Used Ingredient Count: {}, Missed Ingredient Count: {}".format(str(x[i]['usedIngredientCount']),str(x[i]['missedIngredientCount']))
        )
        e.set_image(url=x[i]['image'])
        await ctx.send("Recipe {}".format(str(i+1)),embed=e)


client.run(keys.botToken)
