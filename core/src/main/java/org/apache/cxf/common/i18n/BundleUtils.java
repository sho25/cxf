begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|MissingResourceException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|PackageUtils
import|;
end_import

begin_comment
comment|/**  * A container for static utility methods related to resource bundle  * naming conventons.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|BundleUtils
block|{
comment|/**      * The default resource bundle naming convention for class is a.b.c is a.b.Messages      */
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_BUNDLE
init|=
literal|".Messages"
decl_stmt|;
comment|/**      * Prevents instantiation.      */
specifier|private
name|BundleUtils
parameter_list|()
block|{     }
comment|/**      * Encapsulates the logic related to naming the default resource bundle      * for a class.      *      * @param cls the Class requiring the bundle      * @return an appropriate ResourceBundle name      */
specifier|public
specifier|static
name|String
name|getBundleName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
comment|// Class.getPackage() can return null, so change to another way to get Package Name
return|return
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
operator|+
name|MESSAGE_BUNDLE
return|;
block|}
comment|/**      * Encapsulates the logic related to naming the resource bundle      * with the given relative name for a class.      *      * @param cls the Class requiring the bundle      * @return an appropriate ResourceBundle name      */
specifier|public
specifier|static
name|String
name|getBundleName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
operator|+
literal|"."
operator|+
name|name
return|;
block|}
comment|/**      * Encapsulates the logic related to locating the default resource bundle      * for a class.      *      * @param cls the Class requiring the bundle      * @return an appropriate ResourceBundle      */
specifier|public
specifier|static
name|ResourceBundle
name|getBundle
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
try|try
block|{
name|ClassLoader
name|loader
init|=
name|getClassLoader
argument_list|(
name|cls
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|,
name|loader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|ex
parameter_list|)
block|{
name|ClassLoader
name|loader
init|=
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|,
name|loader
argument_list|)
return|;
block|}
block|}
comment|/**      * Encapsulates the logic related to locating the resource bundle with the given      * relative name for a class.      *      * @param cls the Class requiring the bundle      * @param name the name of the resource      * @return an appropriate ResourceBundle      */
specifier|public
specifier|static
name|ResourceBundle
name|getBundle
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|ClassLoader
name|loader
init|=
name|getClassLoader
argument_list|(
name|cls
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|,
name|name
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|,
name|name
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|,
name|loader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|ex
parameter_list|)
block|{
name|ClassLoader
name|loader
init|=
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|,
name|name
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|getBundleName
argument_list|(
name|cls
argument_list|,
name|name
argument_list|)
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|,
name|loader
argument_list|)
return|;
block|}
block|}
comment|/**      * Encapsulates the logic to format a string based on the key in the resource bundle      *      * @param b Resource bundle to use      * @param key The key in the bundle to lookup      * @param params the params to expand into the string      * @return the formatted string      */
specifier|public
specifier|static
name|String
name|getFormattedString
parameter_list|(
name|ResourceBundle
name|b
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
return|return
name|MessageFormat
operator|.
name|format
argument_list|(
name|b
operator|.
name|getString
argument_list|(
name|key
argument_list|)
argument_list|,
name|params
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ClassLoader
name|getContextClassLoader
parameter_list|()
block|{
specifier|final
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|ClassLoader
name|getClassLoader
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
specifier|final
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

