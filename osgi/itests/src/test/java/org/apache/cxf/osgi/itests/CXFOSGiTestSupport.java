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
name|osgi
operator|.
name|itests
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FutureTask
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|ProbeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|TestProbeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|options
operator|.
name|MavenUrlReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|composite
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|systemProperty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|when
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|editConfigurationFilePut
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|features
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|karafDistributionConfiguration
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|CXFOSGiTestSupport
block|{
specifier|static
specifier|final
name|String
name|KARAF_VERSION
init|=
literal|"2.3.5"
decl_stmt|;
specifier|static
specifier|final
name|Long
name|COMMAND_TIMEOUT
init|=
literal|10000L
decl_stmt|;
specifier|static
specifier|final
name|Long
name|DEFAULT_TIMEOUT
init|=
literal|20000L
decl_stmt|;
specifier|static
specifier|final
name|Long
name|SERVICE_TIMEOUT
init|=
literal|30000L
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|FeaturesService
name|featureService
decl_stmt|;
specifier|protected
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
specifier|protected
name|MavenUrlReference
name|cxfUrl
decl_stmt|;
specifier|protected
name|MavenUrlReference
name|karafUrl
decl_stmt|;
comment|/**      * @param probe      * @return      */
annotation|@
name|ProbeBuilder
specifier|public
name|TestProbeBuilder
name|probeConfiguration
parameter_list|(
name|TestProbeBuilder
name|probe
parameter_list|)
block|{
name|probe
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*,org.apache.felix.service.*;status=provisional"
argument_list|)
expr_stmt|;
return|return
name|probe
return|;
block|}
comment|/**      * Create an {@link org.ops4j.pax.exam.Option} for using a .      *       * @return      */
specifier|protected
name|Option
name|cxfBaseConfig
parameter_list|()
block|{
name|karafUrl
operator|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-karaf"
argument_list|)
operator|.
name|version
argument_list|(
name|KARAF_VERSION
argument_list|)
operator|.
name|type
argument_list|(
literal|"tar.gz"
argument_list|)
expr_stmt|;
name|cxfUrl
operator|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.cxf.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-cxf"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features"
argument_list|)
expr_stmt|;
name|String
name|localRepo
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"localRepository"
argument_list|)
decl_stmt|;
return|return
name|composite
argument_list|(
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|karafUrl
argument_list|)
operator|.
name|karafVersion
argument_list|(
name|KARAF_VERSION
argument_list|)
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
operator|.
name|useDeployFolder
argument_list|(
literal|false
argument_list|)
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/paxexam/"
argument_list|)
argument_list|)
argument_list|,
name|features
argument_list|(
name|cxfUrl
argument_list|,
literal|"cxf-core"
argument_list|,
literal|"cxf-jaxws"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"java.awt.headless"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|,
name|when
argument_list|(
name|localRepo
operator|!=
literal|null
argument_list|)
operator|.
name|useOptions
argument_list|(
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|,
name|localRepo
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Executes a shell command and returns output as a String. Commands have a default timeout of 10 seconds.      *       * @param command      * @return      */
specifier|protected
name|String
name|executeCommand
parameter_list|(
specifier|final
name|String
name|command
parameter_list|)
block|{
return|return
name|executeCommand
argument_list|(
name|command
argument_list|,
name|COMMAND_TIMEOUT
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Executes a shell command and returns output as a String. Commands have a default timeout of 10 seconds.      *       * @param command The command to execute.      * @param timeout The amount of time in millis to wait for the command to execute.      * @param silent Specifies if the command should be displayed in the screen.      * @return      */
specifier|protected
name|String
name|executeCommand
parameter_list|(
specifier|final
name|String
name|command
parameter_list|,
specifier|final
name|Long
name|timeout
parameter_list|,
specifier|final
name|Boolean
name|silent
parameter_list|)
block|{
name|String
name|response
decl_stmt|;
specifier|final
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|PrintStream
name|printStream
init|=
operator|new
name|PrintStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
decl_stmt|;
specifier|final
name|CommandProcessor
name|commandProcessor
init|=
name|getOsgiService
argument_list|(
name|CommandProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|CommandSession
name|commandSession
init|=
name|commandProcessor
operator|.
name|createSession
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|printStream
argument_list|,
name|System
operator|.
name|err
argument_list|)
decl_stmt|;
name|FutureTask
argument_list|<
name|String
argument_list|>
name|commandFuture
init|=
operator|new
name|FutureTask
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|silent
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
name|commandSession
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
name|printStream
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
name|commandFuture
argument_list|)
expr_stmt|;
name|response
operator|=
name|commandFuture
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|response
operator|=
literal|"SHELL COMMAND TIMED OUT: "
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
comment|/**      * Executes multiple commands inside a Single Session. Commands have a default timeout of 10 seconds.      *       * @param commands      * @return      */
specifier|protected
name|String
name|executeCommands
parameter_list|(
specifier|final
name|String
modifier|...
name|commands
parameter_list|)
block|{
name|String
name|response
decl_stmt|;
specifier|final
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|PrintStream
name|printStream
init|=
operator|new
name|PrintStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
decl_stmt|;
specifier|final
name|CommandProcessor
name|commandProcessor
init|=
name|getOsgiService
argument_list|(
name|CommandProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|CommandSession
name|commandSession
init|=
name|commandProcessor
operator|.
name|createSession
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|printStream
argument_list|,
name|System
operator|.
name|err
argument_list|)
decl_stmt|;
name|FutureTask
argument_list|<
name|String
argument_list|>
name|commandFuture
init|=
operator|new
name|FutureTask
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
block|{
try|try
block|{
for|for
control|(
name|String
name|command
range|:
name|commands
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|commandSession
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
return|return
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
name|commandFuture
argument_list|)
expr_stmt|;
name|response
operator|=
name|commandFuture
operator|.
name|get
argument_list|(
name|COMMAND_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|response
operator|=
literal|"SHELL COMMAND TIMED OUT: "
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
specifier|protected
name|Bundle
name|getInstalledBundle
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|symbolicName
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Bundle: "
operator|+
name|b
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Bundle "
operator|+
name|symbolicName
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
comment|/*      * Explode the dictionary into a ,-delimited list of key=value pairs      */
specifier|private
specifier|static
name|String
name|explode
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dictionary
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|dictionary
operator|.
name|keys
argument_list|()
decl_stmt|;
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s=%s"
argument_list|,
name|key
argument_list|,
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|ServiceTracker
name|tracker
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|flt
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|filter
operator|+
literal|"))"
expr_stmt|;
block|}
block|}
else|else
block|{
name|flt
operator|=
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
name|Filter
name|osgiFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
name|flt
argument_list|)
decl_stmt|;
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|osgiFilter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Note that the tracker is not closed to keep the reference
comment|// This is buggy, as the service reference may change i think
name|Object
name|svc
init|=
name|type
operator|.
name|cast
argument_list|(
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|svc
operator|==
literal|null
condition|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dic
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Test bundle headers: "
operator|+
name|explode
argument_list|(
name|dic
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"ServiceReference: "
operator|+
name|ref
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
name|flt
argument_list|)
argument_list|)
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Filtered ServiceReference: "
operator|+
name|ref
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Gave up waiting for service "
operator|+
name|flt
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|svc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid filter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Finds a free port starting from the give port numner.      *       * @return      */
specifier|protected
name|int
name|getFreePort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
while|while
condition|(
operator|!
name|isPortAvailable
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|port
operator|++
expr_stmt|;
block|}
return|return
name|port
return|;
block|}
comment|/**      * Returns true if port is available for use.      *       * @param port      * @return      */
specifier|public
specifier|static
name|boolean
name|isPortAvailable
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|ServerSocket
name|ss
init|=
literal|null
decl_stmt|;
name|DatagramSocket
name|ds
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ss
operator|=
operator|new
name|ServerSocket
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ds
operator|=
operator|new
name|DatagramSocket
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
if|if
condition|(
name|ds
operator|!=
literal|null
condition|)
block|{
name|ds
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ss
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ss
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|/* should not be thrown */
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Provides an iterable collection of references, even if the original array is null      */
specifier|private
specifier|static
name|Collection
argument_list|<
name|ServiceReference
argument_list|>
name|asCollection
parameter_list|(
name|ServiceReference
index|[]
name|references
parameter_list|)
block|{
return|return
name|references
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
name|references
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|ServiceReference
operator|>
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|void
name|assertBundleStarted
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|findBundleByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Bundle "
operator|+
name|name
operator|+
literal|" should be installed"
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Bundle "
operator|+
name|name
operator|+
literal|" should be started"
argument_list|,
name|Bundle
operator|.
name|ACTIVE
argument_list|,
name|bundle
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Bundle
name|findBundleByName
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|symbolicName
argument_list|)
condition|)
block|{
return|return
name|bundle
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|assertServicePublished
parameter_list|(
name|String
name|filter
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
try|try
block|{
name|Filter
name|serviceFilter
init|=
name|bundleContext
operator|.
name|createFilter
argument_list|(
name|filter
argument_list|)
decl_stmt|;
name|ServiceTracker
name|tracker
init|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|serviceFilter
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|Object
name|service
init|=
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected service with filter "
operator|+
name|filter
operator|+
literal|" was not found"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unexpected exception occured"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|assertBlueprintNamespacePublished
parameter_list|(
name|String
name|namespace
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
name|assertServicePublished
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"(&(objectClass=org.apache.aries.blueprint.NamespaceHandler)"
operator|+
literal|"(osgi.service.blueprint.namespace=%s))"
argument_list|,
name|namespace
argument_list|)
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

