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
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_comment
comment|/**  * This is called from LogUtils as LogUtils is almost always one of the VERY  * first classes loaded in CXF so we can try and register to hacks/workarounds  * for various bugs in the JDK.  *   * Much of this is taken from work the Tomcat folks have done to find  * places where memory leaks and jars are locked and such.  * See:  * http://svn.apache.org/viewvc/tomcat/trunk/java/org/apache/catalina/  * core/JreMemoryLeakPreventionListener.java  *   */
end_comment

begin_class
specifier|final
class|class
name|JDKBugHacks
block|{
specifier|private
name|JDKBugHacks
parameter_list|()
block|{
comment|//not constructed
block|}
specifier|public
specifier|static
name|void
name|doHacks
parameter_list|()
block|{
try|try
block|{
name|ClassLoader
name|orig
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// Use the system classloader as the victim for all this
comment|// ClassLoader pinning we're about to do.
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|//Trigger a call to sun.awt.AppContext.getAppContext()
name|ImageIO
operator|.
name|getCacheDirectory
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
try|try
block|{
comment|// Several components end up opening JarURLConnections without first
comment|// disabling caching. This effectively locks the file.
comment|// JAXB does this and thus affects us pretty badly.
comment|// Doesn't matter that this JAR doesn't exist - just as long as
comment|// the URL is well-formed
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"jar:file://dummy.jar!/"
argument_list|)
decl_stmt|;
name|URLConnection
name|uConn
init|=
operator|new
name|URLConnection
argument_list|(
name|url
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|IOException
block|{
comment|// NOOP
block|}
block|}
decl_stmt|;
name|uConn
operator|.
name|setDefaultUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
try|try
block|{
comment|//DocumentBuilderFactory seems to SOMETIMES pin the classloader
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
comment|// Several components end up calling:
comment|// sun.misc.GC.requestLatency(long)
comment|//
comment|// Those libraries / components known to trigger memory leaks due to
comment|// eventual calls to requestLatency(long) are:
comment|// - javax.management.remote.rmi.RMIConnectorServer.start()
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"sun.misc.GC"
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|clazz
operator|.
name|getDeclaredMethod
argument_list|(
literal|"requestLatency"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Long
operator|.
name|TYPE
block|}
argument_list|)
decl_stmt|;
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
literal|3600000
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
comment|// Calling getPolicy retains a static reference to the context
comment|// class loader.
try|try
block|{
comment|// Policy.getPolicy();
name|Class
argument_list|<
name|?
argument_list|>
name|policyClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"javax.security.auth.Policy"
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|policyClass
operator|.
name|getMethod
argument_list|(
literal|"getPolicy"
argument_list|)
decl_stmt|;
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
comment|// Initializing javax.security.auth.login.Configuration retains a static reference
comment|// to the context class loader.
name|Class
operator|.
name|forName
argument_list|(
literal|"javax.security.auth.login.Configuration"
argument_list|,
literal|true
argument_list|,
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
comment|// Creating a MessageDigest during web application startup
comment|// initializes the Java Cryptography Architecture. Under certain
comment|// conditions this starts a Token poller thread with TCCL equal
comment|// to the web application class loader.
name|java
operator|.
name|security
operator|.
name|Security
operator|.
name|getProviders
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|orig
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
end_class

end_unit

