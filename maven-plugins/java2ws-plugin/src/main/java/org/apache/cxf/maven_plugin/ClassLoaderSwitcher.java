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
name|maven_plugin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|project
operator|.
name|MavenProject
import|;
end_import

begin_comment
comment|/**  * Manages switching to the classloader needed for creating the java sources and restoring the old classloader  * when finished  */
end_comment

begin_class
specifier|public
class|class
name|ClassLoaderSwitcher
block|{
specifier|private
name|Log
name|log
decl_stmt|;
specifier|private
name|String
name|origClassPath
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|origProps
decl_stmt|;
specifier|private
name|ClassLoader
name|origContextClassloader
decl_stmt|;
specifier|public
name|ClassLoaderSwitcher
parameter_list|(
name|Log
name|log
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
block|}
comment|/**      * Create and set the classloader that is needed for creating the java sources from wsdl      *       * @param project      * @param useCompileClasspath      * @param classesDir      */
specifier|public
name|String
name|switchClassLoader
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|boolean
name|useCompileClasspath
parameter_list|,
name|String
name|classpath
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|classpathElements
parameter_list|)
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urlList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|buf
operator|.
name|append
argument_list|(
name|classpath
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
name|urlList
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
for|for
control|(
name|Object
name|classpathElement
range|:
name|classpathElements
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|classpathElement
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
name|List
argument_list|<
name|?
argument_list|>
name|artifacts
init|=
name|useCompileClasspath
condition|?
name|project
operator|.
name|getCompileArtifacts
argument_list|()
else|:
name|project
operator|.
name|getTestArtifacts
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|a
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|artifacts
argument_list|,
name|Artifact
operator|.
name|class
argument_list|)
control|)
block|{
try|try
block|{
if|if
condition|(
name|a
operator|.
name|getFile
argument_list|()
operator|!=
literal|null
operator|&&
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|urlList
operator|.
name|add
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
comment|// System.out.println("     " +
comment|// a.getFile().getAbsolutePath());
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|origContextClassloader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|ClassLoader
name|loader
init|=
name|ClassLoaderUtils
operator|.
name|getURLClassLoader
argument_list|(
name|urlList
argument_list|,
name|origContextClassloader
argument_list|)
decl_stmt|;
name|String
name|newCp
init|=
name|buf
operator|.
name|toString
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Classpath: "
operator|+
name|urlList
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|origProps
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|origClassPath
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|newCp
argument_list|)
expr_stmt|;
return|return
name|newCp
return|;
block|}
comment|/**      * Restore the old classloader      */
specifier|public
name|void
name|restoreClassLoader
parameter_list|()
block|{
if|if
condition|(
name|origContextClassloader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|origContextClassloader
argument_list|)
expr_stmt|;
name|origContextClassloader
operator|=
literal|null
expr_stmt|;
comment|// don't hold a reference.
block|}
if|if
condition|(
name|origClassPath
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|origClassPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origProps
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|newProps
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|newProps
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|origProps
operator|.
name|containsKey
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|origProps
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

