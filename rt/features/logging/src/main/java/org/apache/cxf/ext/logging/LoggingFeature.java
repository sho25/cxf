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
name|ext
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|annotations
operator|.
name|Provider
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
name|annotations
operator|.
name|Provider
operator|.
name|Type
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEventSender
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|PrettyLoggingFilter
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
name|ext
operator|.
name|logging
operator|.
name|slf4j
operator|.
name|Slf4jEventSender
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
name|ext
operator|.
name|logging
operator|.
name|slf4j
operator|.
name|Slf4jVerboseEventSender
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
name|feature
operator|.
name|AbstractPortableFeature
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
name|feature
operator|.
name|DelegatingFeature
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
name|interceptor
operator|.
name|InterceptorProvider
import|;
end_import

begin_comment
comment|/**  * This class is used to control message-on-the-wire logging.  * By attaching this feature to an endpoint, you  * can specify logging. If this feature is present, an endpoint will log input  * and output of ordinary and log messages.  *<pre>  *<![CDATA[<jaxws:endpoint ...><jaxws:features><bean class="org.apache.cxf.ext.logging.LoggingFeature"/></jaxws:features></jaxws:endpoint>   ]]></pre>  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
annotation|@
name|Provider
argument_list|(
name|value
operator|=
name|Type
operator|.
name|Feature
argument_list|)
specifier|public
class|class
name|LoggingFeature
extends|extends
name|DelegatingFeature
argument_list|<
name|LoggingFeature
operator|.
name|Portable
argument_list|>
block|{
specifier|public
name|LoggingFeature
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|Portable
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|delegate
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInMemThreshold
parameter_list|(
name|long
name|inMemThreshold
parameter_list|)
block|{
name|delegate
operator|.
name|setInMemThreshold
argument_list|(
name|inMemThreshold
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSender
parameter_list|(
name|LogEventSender
name|sender
parameter_list|)
block|{
name|delegate
operator|.
name|setSender
argument_list|(
name|sender
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInSender
parameter_list|(
name|LogEventSender
name|s
parameter_list|)
block|{
name|delegate
operator|.
name|setInSender
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutSender
parameter_list|(
name|LogEventSender
name|s
parameter_list|)
block|{
name|delegate
operator|.
name|setOutSender
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPrettyLogging
parameter_list|(
name|boolean
name|prettyLogging
parameter_list|)
block|{
name|delegate
operator|.
name|setPrettyLogging
argument_list|(
name|prettyLogging
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLogBinary
parameter_list|(
name|boolean
name|logBinary
parameter_list|)
block|{
name|delegate
operator|.
name|setLogBinary
argument_list|(
name|logBinary
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLogMultipart
parameter_list|(
name|boolean
name|logMultipart
parameter_list|)
block|{
name|delegate
operator|.
name|setLogMultipart
argument_list|(
name|logMultipart
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setVerbose
parameter_list|(
name|boolean
name|verbose
parameter_list|)
block|{
name|delegate
operator|.
name|setVerbose
argument_list|(
name|verbose
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addInBinaryContentMediaTypes
parameter_list|(
name|String
name|mediaTypes
parameter_list|)
block|{
name|delegate
operator|.
name|addInBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addOutBinaryContentMediaTypes
parameter_list|(
name|String
name|mediaTypes
parameter_list|)
block|{
name|delegate
operator|.
name|addOutBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addBinaryContentMediaTypes
parameter_list|(
name|String
name|mediaTypes
parameter_list|)
block|{
name|delegate
operator|.
name|addBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provider
argument_list|(
name|value
operator|=
name|Type
operator|.
name|Feature
argument_list|)
specifier|public
specifier|static
class|class
name|Portable
implements|implements
name|AbstractPortableFeature
block|{
specifier|private
name|LoggingInInterceptor
name|in
decl_stmt|;
specifier|private
name|LoggingOutInterceptor
name|out
decl_stmt|;
specifier|private
name|PrettyLoggingFilter
name|inPrettyFilter
decl_stmt|;
specifier|private
name|PrettyLoggingFilter
name|outPrettyFilter
decl_stmt|;
specifier|public
name|Portable
parameter_list|()
block|{
name|LogEventSender
name|sender
init|=
operator|new
name|Slf4jVerboseEventSender
argument_list|()
decl_stmt|;
name|inPrettyFilter
operator|=
operator|new
name|PrettyLoggingFilter
argument_list|(
name|sender
argument_list|)
expr_stmt|;
name|outPrettyFilter
operator|=
operator|new
name|PrettyLoggingFilter
argument_list|(
name|sender
argument_list|)
expr_stmt|;
name|in
operator|=
operator|new
name|LoggingInInterceptor
argument_list|(
name|inPrettyFilter
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|LoggingOutInterceptor
argument_list|(
name|outPrettyFilter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|doInitializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|in
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|out
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInMemThreshold
parameter_list|(
name|long
name|inMemThreshold
parameter_list|)
block|{
name|in
operator|.
name|setInMemThreshold
argument_list|(
name|inMemThreshold
argument_list|)
expr_stmt|;
name|out
operator|.
name|setInMemThreshold
argument_list|(
name|inMemThreshold
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSender
parameter_list|(
name|LogEventSender
name|sender
parameter_list|)
block|{
name|this
operator|.
name|inPrettyFilter
operator|.
name|setNext
argument_list|(
name|sender
argument_list|)
expr_stmt|;
name|this
operator|.
name|outPrettyFilter
operator|.
name|setNext
argument_list|(
name|sender
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInSender
parameter_list|(
name|LogEventSender
name|s
parameter_list|)
block|{
name|this
operator|.
name|inPrettyFilter
operator|.
name|setNext
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutSender
parameter_list|(
name|LogEventSender
name|s
parameter_list|)
block|{
name|this
operator|.
name|outPrettyFilter
operator|.
name|setNext
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPrettyLogging
parameter_list|(
name|boolean
name|prettyLogging
parameter_list|)
block|{
name|this
operator|.
name|inPrettyFilter
operator|.
name|setPrettyLogging
argument_list|(
name|prettyLogging
argument_list|)
expr_stmt|;
name|this
operator|.
name|outPrettyFilter
operator|.
name|setPrettyLogging
argument_list|(
name|prettyLogging
argument_list|)
expr_stmt|;
block|}
comment|/**          * Log binary content?          * @param logBinary defaults to false          */
specifier|public
name|void
name|setLogBinary
parameter_list|(
name|boolean
name|logBinary
parameter_list|)
block|{
name|in
operator|.
name|setLogBinary
argument_list|(
name|logBinary
argument_list|)
expr_stmt|;
name|out
operator|.
name|setLogBinary
argument_list|(
name|logBinary
argument_list|)
expr_stmt|;
block|}
comment|/**          * Log multipart content?          * @param logMultipart defaults to true          */
specifier|public
name|void
name|setLogMultipart
parameter_list|(
name|boolean
name|logMultipart
parameter_list|)
block|{
name|in
operator|.
name|setLogMultipart
argument_list|(
name|logMultipart
argument_list|)
expr_stmt|;
name|out
operator|.
name|setLogMultipart
argument_list|(
name|logMultipart
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setVerbose
parameter_list|(
name|boolean
name|verbose
parameter_list|)
block|{
name|setSender
argument_list|(
name|verbose
condition|?
operator|new
name|Slf4jVerboseEventSender
argument_list|()
else|:
operator|new
name|Slf4jEventSender
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**          * Add additional binary media types to the default values in the LoggingInInterceptor.          * Content for these types will not be logged.          * For example:          *<pre>          *&lt;bean id="loggingFeature" class="org.apache.cxf.ext.logging.LoggingFeature"&gt;          *&lt;property name="addInBinaryContentMediaTypes" value="audio/mpeg;application/zip"/&gt;          *&lt;/bean&gt;          *</pre>          * @param mediaTypes list of mediaTypes. symbol ; - delimeter          */
specifier|public
name|void
name|addInBinaryContentMediaTypes
parameter_list|(
name|String
name|mediaTypes
parameter_list|)
block|{
name|in
operator|.
name|addBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
block|}
comment|/**          * Add additional binary media types to the default values in the LoggingOutInterceptor.          * Content for these types will not be logged.          * For example:          *<pre>          *&lt;bean id="loggingFeature" class="org.apache.cxf.ext.logging.LoggingFeature"&gt;          *&lt;property name="addOutBinaryContentMediaTypes" value="audio/mpeg;application/zip"/&gt;          *&lt;/bean&gt;          *</pre>          * @param mediaTypes list of mediaTypes. symbol ; - delimeter          */
specifier|public
name|void
name|addOutBinaryContentMediaTypes
parameter_list|(
name|String
name|mediaTypes
parameter_list|)
block|{
name|out
operator|.
name|addBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
block|}
comment|/**          * Add additional binary media types to the default values for both logging interceptors          * Content for these types will not be logged.          * For example:          *<pre>          *&lt;bean id="loggingFeature" class="org.apache.cxf.ext.logging.LoggingFeature"&gt;          *&lt;property name="addBinaryContentMediaTypes" value="audio/mpeg;application/zip"/&gt;          *&lt;/bean&gt;          *</pre>          * @param mediaTypes list of mediaTypes. symbol ; - delimeter          */
specifier|public
name|void
name|addBinaryContentMediaTypes
parameter_list|(
name|String
name|mediaTypes
parameter_list|)
block|{
name|addInBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
name|addOutBinaryContentMediaTypes
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

