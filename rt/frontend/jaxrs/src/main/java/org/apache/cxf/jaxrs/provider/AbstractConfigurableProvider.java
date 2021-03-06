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
name|jaxrs
operator|.
name|provider
package|;
end_package

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
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|NoContentException
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
name|BusFactory
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
name|i18n
operator|.
name|BundleUtils
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
name|logging
operator|.
name|LogUtils
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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractConfigurableProvider
block|{
specifier|protected
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AbstractJAXBProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractJAXBProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|consumeMediaTypes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|produceMediaTypes
decl_stmt|;
specifier|private
name|boolean
name|enableBuffering
decl_stmt|;
specifier|private
name|boolean
name|enableStreaming
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
comment|/**      * Sets the Bus      * @param b      */
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
block|}
comment|/**      * Gets the Bus.      * Providers may use the bus to resolve resource references.      * Example:      * ResourceUtils.getResourceStream(reference, this.getBus())      *      * @return      */
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
operator|!=
literal|null
condition|?
name|bus
else|:
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
return|;
block|}
comment|/**      * Sets custom Consumes media types; can be used to override static      * {@link Consumes} annotation value set on the provider.      * @param types the media types      */
specifier|public
name|void
name|setConsumeMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|consumeMediaTypes
operator|=
name|types
expr_stmt|;
block|}
comment|/**      * Gets the custom Consumes media types      * @return media types      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConsumeMediaTypes
parameter_list|()
block|{
return|return
name|consumeMediaTypes
return|;
block|}
comment|/**      * Sets custom Produces media types; can be used to override static      * {@link Produces} annotation value set on the provider.      * @param types the media types      */
specifier|public
name|void
name|setProduceMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|produceMediaTypes
operator|=
name|types
expr_stmt|;
block|}
comment|/**      * Gets the custom Produces media types      * @return media types      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProduceMediaTypes
parameter_list|()
block|{
return|return
name|produceMediaTypes
return|;
block|}
comment|/**      * Enables the buffering mode. If set to true then the runtime will ensure      * that the provider writes to a cached stream.      *      * For example, the JAXB marshalling process may fail after the initial XML      * tags have already been written out to the HTTP output stream. Enabling      * the buffering ensures no incomplete payloads are sent back to clients      * in case of marshalling errors at the cost of the initial buffering - which      * might be negligible for small payloads.      *      * @param enableBuf the value of the buffering mode, false is default.      */
specifier|public
name|void
name|setEnableBuffering
parameter_list|(
name|boolean
name|enableBuf
parameter_list|)
block|{
name|enableBuffering
operator|=
name|enableBuf
expr_stmt|;
block|}
comment|/**      * Gets the value of the buffering mode      * @return true if the buffering is enabled      */
specifier|public
name|boolean
name|getEnableBuffering
parameter_list|()
block|{
return|return
name|enableBuffering
return|;
block|}
comment|/**      * Enables the support for streaming. XML-aware providers which prefer      * writing to Stax XMLStreamWriter can set this value to true. Additionally,      * if the streaming and the buffering modes are enabled, the runtime will      * ensure the XMLStreamWriter events are cached properly.      * @param enableStream the value of the streaming mode, false is default.      */
specifier|public
name|void
name|setEnableStreaming
parameter_list|(
name|boolean
name|enableStream
parameter_list|)
block|{
name|enableStreaming
operator|=
name|enableStream
expr_stmt|;
block|}
comment|/**      * Gets the value of the streaming mode      * @return true if the streaming is enabled      */
specifier|public
name|boolean
name|getEnableStreaming
parameter_list|()
block|{
return|return
name|enableStreaming
return|;
block|}
comment|/**      * Gives providers a chance to introspect the JAX-RS model classes.      * For example, the JAXB provider may use the model classes to create      * a single composite JAXBContext supporting all the JAXB-annotated      * root resource classes/types.      *      * @param resources      */
specifier|public
name|void
name|init
parameter_list|(
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
parameter_list|)
block|{
comment|// complete
block|}
specifier|protected
name|boolean
name|isPayloadEmpty
parameter_list|(
name|HttpHeaders
name|headers
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
return|return
name|isPayloadEmpty
argument_list|(
name|headers
operator|.
name|getRequestHeaders
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|boolean
name|isPayloadEmpty
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
return|return
name|HttpUtils
operator|.
name|isPayloadEmpty
argument_list|(
name|headers
argument_list|)
return|;
block|}
specifier|protected
name|void
name|reportEmptyContentLength
parameter_list|()
throws|throws
name|NoContentException
block|{
name|String
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"EMPTY_BODY"
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|NoContentException
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

