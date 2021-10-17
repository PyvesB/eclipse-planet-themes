# Planet Themes 
<a href="https://github.com/PyvesB/eclipse-planet-themes/blob/master/LICENSE">
<img src ="https://img.shields.io/github/license/PyvesB/eclipse-planet-themes.svg" />
</a>
<a href="https://github.com/PyvesB/eclipse-planet-themes/issues">
<img src ="https://img.shields.io/github/issues/PyvesB/eclipse-planet-themes.svg" />
</a>
<a href="https://github.com/PyvesB/eclipse-planet-themes/stargazers">
<img src ="https://img.shields.io/github/stars/PyvesB/eclipse-planet-themes.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/planet-themes">
<img src ="https://img.shields.io/eclipse-marketplace/v/planet-themes.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/planet-themes">
<img src ="https://img.shields.io/eclipse-marketplace/favorites/planet-themes.svg" />
</a>
<a href="https://marketplace.eclipse.org/content/planet-themes">
<img src ="https://img.shields.io/eclipse-marketplace/dt/planet-themes.svg" />
</a>

**Collection of light and dark Eclipse themes, inspired by planets of the Solar System!**

<p align="center" style="font-size:6px;">
<br />
<img src ="https://github.com/PyvesB/eclipse-planet-themes/blob/master/images/neptune.png?raw=true" width="32%" />
<img src ="https://github.com/PyvesB/eclipse-planet-themes/blob/master/images/pluto.png?raw=true" width="32%" />
<img src ="https://github.com/PyvesB/eclipse-planet-themes/blob/master/images/moon.png?raw=true" width="32%" />
<br />
<i><sub>Left to right: the Neptune, Pluto and Moon themes!</sub></i>
</p>

## :moon: Features at a glance

Three themes are currently available:
* Moon: familiar-looking but not an actual planet. Light-grey, by and large sticks to Eclipse's classic color scheme.
* Neptune: dark blue theme with some red reflections from the distant sun.
* Pluto: not much light reaches this distant dwarf planet, leading to a resolutely dark theme. The presence of purple has yet to be explained by astronomers.

A few words to summarise the planetary ambitions:
* compact: few borders, less visual artifacts, hidden buttons and flat scrollbars on Windows.
* readable: easy on the eyes with low glare, balanced contrasts and an integrated editor font with programming ligatures.
* outstanding: not a revolution, but hopefully a nice twist to the traditional Eclipse look and feel.

Check out what's new in the [latest releases](https://github.com/PyvesB/eclipse-planet-themes/releases)!

## :cd: Plugin installation

Note that all themes require Eclipse Oxygen (4.7) or more recent.

You can download and install the plugin via the [Eclipse Marketplace](https://marketplace.eclipse.org/content/planet-themes/), or simply drag the below button to your running Eclipse workspace:

<p align="center">
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=5176732" class="drag" title="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client"><img width="100" typeof="foaf:Image" class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.svg" alt="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client" /></a>
</p>

Alternatively, use the following update site: `https://cdn.jsdelivr.net/gh/PyvesB/eclipse-planet-themes@updatesite/`

Once installed, open Eclipse's preferences, navigate to `General` -> `Appearance` and select the planet of your choice in the `Theme` dropdown. Restart Eclipse and enjoy your new theme!

## :star: Support and feedback

Building themes is hard work. Something isn't to your taste? Thought of a cool idea? Found a problem or need some help? Simply open an [**issue**](https://github.com/PyvesB/eclipse-planet-themes/issues)!

Find the project useful, fun or interesting? **Star** the repository by clicking on the icon on the top right of this page!

## :computer: Code contributions

Want to improve the existing planets or add a new one? Contributions are more than welcome, open a **pull request** and share your CSS!

Setting up your own working copy of the project is easy:
* Download the [RCP and RAP Developers](https://eclipse.org/downloads/eclipse-packages/) version of Eclipse.
* Fork the repository by clicking on the *Fork* icon on the top right of this page and clone it locally.
* In Eclipse, go to `File` -> `Import...` -> `General` -> `Existing Projects into Workspace`.
* In the `Select root directory` field, indicate the location where you checked out the planet-themes repository.
* Ensure `Search for nested projects` is enabled, select all projects in the `Projects` field and click `Finish`.
* Open `planet-themes-targetplatform.target` and click `Set as Active Target Platform`.
* You're ready to go! You can now either launch an instance of Eclipse running the plugin by right-clicking on the plugin project and selecting `Run As` -> `Eclipse Application`, or you can export a plugin archive file by selecting `Export` -> `Deployable plug-ins and fragments`.

## :balance_scale: License and acknowledgements

Planet Themes are licensed under Eclipse Public License - v 2.0.

The following third-party projects are being used, many thanks to them:
* [Xiliary](https://github.com/fappel/xiliary) by [Frank Appel](https://github.com/fappel): flat scrollbars on Windows (Eclipse Public License).
* [Fira Code](https://github.com/tonsky/FiraCode) by [Nikita Prokopov](https://github.com/tonsky): editor font (Open Font License).
