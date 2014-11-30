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
name|message
package|;
end_package

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
name|Set
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
name|InterceptorChain
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
name|transport
operator|.
name|Destination
import|;
end_import

begin_comment
comment|/**  * The base interface for all all message implementations.   * All message objects passed to interceptors use this interface.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Message
extends|extends
name|StringMap
block|{
name|String
name|TRANSPORT
init|=
literal|"org.apache.cxf.transport"
decl_stmt|;
comment|/*      * Boolean property which can be used to check that the current request      * is part of the SOAP (JAX-WS) or non-SOAP/REST (JAX-RS) execution context.      */
name|String
name|REST_MESSAGE
init|=
literal|"org.apache.cxf.rest.message"
decl_stmt|;
comment|/**      * Boolean property specifying if the message is a request message.      */
name|String
name|REQUESTOR_ROLE
init|=
literal|"org.apache.cxf.client"
decl_stmt|;
comment|/**      * Boolean property specifying if the message is inbound.      */
name|String
name|INBOUND_MESSAGE
init|=
literal|"org.apache.cxf.message.inbound"
decl_stmt|;
comment|/**      * A Map keyed by a string that stores optional context information       * associated with the invocation that spawned the message.      */
name|String
name|INVOCATION_CONTEXT
init|=
literal|"org.apache.cxf.invocation.context"
decl_stmt|;
comment|/**      *  Current Service Object      */
name|String
name|SERVICE_OBJECT
init|=
literal|"org.apache.cxf.service.object"
decl_stmt|;
comment|/**      * A Map containing the MIME headers for a SOAP message.      */
name|String
name|MIME_HEADERS
init|=
literal|"org.apache.cxf.mime.headers"
decl_stmt|;
comment|/**      * Boolean property specifying if the server should send the response       * asynchronously.      */
name|String
name|ASYNC_POST_RESPONSE_DISPATCH
init|=
literal|"org.apache.cxf.async.post.response.dispatch"
decl_stmt|;
comment|/**      * Boolean property specifying if this message arrived via a       * decoupled endpoint.      */
name|String
name|DECOUPLED_CHANNEL_MESSAGE
init|=
literal|"decoupled.channel.message"
decl_stmt|;
name|String
name|PARTIAL_RESPONSE_MESSAGE
init|=
literal|"org.apache.cxf.partial.response"
decl_stmt|;
name|String
name|EMPTY_PARTIAL_RESPONSE_MESSAGE
init|=
literal|"org.apache.cxf.partial.response.empty"
decl_stmt|;
name|String
name|ONE_WAY_REQUEST
init|=
literal|"OnewayRequest"
decl_stmt|;
comment|/**      * Boolean property specifying if oneWay response must be processed.      */
name|String
name|PROCESS_ONEWAY_RESPONSE
init|=
literal|"org.apache.cxf.transport.processOneWayResponse"
decl_stmt|;
comment|/**      * Boolean property specifying if the thread which runs a request is       * different to the thread which created this Message.      */
name|String
name|THREAD_CONTEXT_SWITCHED
init|=
literal|"thread.context.switched"
decl_stmt|;
name|String
name|ROBUST_ONEWAY
init|=
literal|"org.apache.cxf.oneway.robust"
decl_stmt|;
name|String
name|HTTP_REQUEST_METHOD
init|=
literal|"org.apache.cxf.request.method"
decl_stmt|;
name|String
name|REQUEST_URI
init|=
literal|"org.apache.cxf.request.uri"
decl_stmt|;
name|String
name|REQUEST_URL
init|=
literal|"org.apache.cxf.request.url"
decl_stmt|;
name|String
name|PROTOCOL_HEADERS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".PROTOCOL_HEADERS"
decl_stmt|;
name|String
name|RESPONSE_CODE
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".RESPONSE_CODE"
decl_stmt|;
name|String
name|ENDPOINT_ADDRESS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ENDPOINT_ADDRESS"
decl_stmt|;
name|String
name|PATH_INFO
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".PATH_INFO"
decl_stmt|;
name|String
name|QUERY_STRING
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".QUERY_STRING"
decl_stmt|;
name|String
name|PROPOGATE_EXCEPTION
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".PROPOGATE_EXCEPTION"
decl_stmt|;
comment|/**      * Boolean property specifying in the runtime is configured to process       * MTOM attachments.      */
name|String
name|MTOM_ENABLED
init|=
literal|"mtom-enabled"
decl_stmt|;
name|String
name|MTOM_THRESHOLD
init|=
literal|"mtom-threshold"
decl_stmt|;
comment|/**      * Runtime schema validation property      */
name|String
name|SCHEMA_VALIDATION_ENABLED
init|=
literal|"schema-validation-enabled"
decl_stmt|;
comment|/**      * The default values for schema validation will be set in the service model using this property      */
name|String
name|SCHEMA_VALIDATION_TYPE
init|=
literal|"schema-validation-type"
decl_stmt|;
comment|/**      * Boolean property specifying if the Java stack trace is returned as a        * SOAP fault message.      */
name|String
name|FAULT_STACKTRACE_ENABLED
init|=
literal|"faultStackTraceEnabled"
decl_stmt|;
comment|/**      * Boolean property specifying if the name of the exception that caused       * the Java stack trace is returned.      */
name|String
name|EXCEPTION_MESSAGE_CAUSE_ENABLED
init|=
literal|"exceptionMessageCauseEnabled"
decl_stmt|;
comment|/**      * A very unique delimiter used for exception with FAULT_STACKTRACE_ENABLED enable,       * which is easy for client to differentiate the cause and stacktrace when unmarsall       * a fault message       */
name|String
name|EXCEPTION_CAUSE_SUFFIX
init|=
literal|"#*#"
decl_stmt|;
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
name|String
name|ACCEPT_CONTENT_TYPE
init|=
literal|"Accept"
decl_stmt|;
name|String
name|BASE_PATH
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".BASE_PATH"
decl_stmt|;
name|String
name|ENCODING
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ENCODING"
decl_stmt|;
name|String
name|FIXED_PARAMETER_ORDER
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"FIXED_PARAMETER_ORDER"
decl_stmt|;
name|String
name|MAINTAIN_SESSION
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".MAINTAIN_SESSION"
decl_stmt|;
name|String
name|ATTACHMENTS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ATTACHMENTS"
decl_stmt|;
name|String
name|WSDL_DESCRIPTION
init|=
literal|"javax.xml.ws.wsdl.description"
decl_stmt|;
name|String
name|WSDL_SERVICE
init|=
literal|"javax.xml.ws.wsdl.service"
decl_stmt|;
name|String
name|WSDL_PORT
init|=
literal|"javax.xml.ws.wsdl.port"
decl_stmt|;
name|String
name|WSDL_INTERFACE
init|=
literal|"javax.xml.ws.wsdl.interface"
decl_stmt|;
name|String
name|WSDL_OPERATION
init|=
literal|"javax.xml.ws.wsdl.operation"
decl_stmt|;
comment|/**      * Some properties to allow adding interceptors to the chain      * on a per-request basis.  All are a Collection<Interceptor>       * These are NOT contextual properties (ie: not searched outside the message).      * They must exist on the message itself at time of Chain creation      */
name|String
name|IN_INTERCEPTORS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".IN_INTERCEPTORS"
decl_stmt|;
name|String
name|OUT_INTERCEPTORS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".OUT_INTERCEPTORS"
decl_stmt|;
name|String
name|FAULT_IN_INTERCEPTORS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".FAULT_IN_INTERCEPTORS"
decl_stmt|;
name|String
name|FAULT_OUT_INTERCEPTORS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".FAULT_OUT_INTERCEPTORS"
decl_stmt|;
comment|/**      * As above, but Collection<InterceptorProvider>       */
name|String
name|INTERCEPTOR_PROVIDERS
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".INTERCEPTOR_PROVIDER"
decl_stmt|;
comment|/*      * The properties to allow configure the client timeout      */
name|String
name|CONNECTION_TIMEOUT
init|=
literal|"javax.xml.ws.client.connectionTimeout"
decl_stmt|;
name|String
name|RECEIVE_TIMEOUT
init|=
literal|"javax.xml.ws.client.receiveTimeout"
decl_stmt|;
name|String
name|getId
parameter_list|()
function_decl|;
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns a live copy of the messages interceptor chain. This is       * useful when an interceptor wants to modify the interceptor chain on the       * fly.      *       * @return the interceptor chain used to process the message      */
name|InterceptorChain
name|getInterceptorChain
parameter_list|()
function_decl|;
name|void
name|setInterceptorChain
parameter_list|(
name|InterceptorChain
name|chain
parameter_list|)
function_decl|;
comment|/**      * @return the associated Destination if message is inbound, null otherwise      */
name|Destination
name|getDestination
parameter_list|()
function_decl|;
name|Exchange
name|getExchange
parameter_list|()
function_decl|;
name|void
name|setExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Retrieve any binary attachments associated with the message.      *        * @return a collection containing the attachments      */
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|getAttachments
parameter_list|()
function_decl|;
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
function_decl|;
comment|/**      * Retrieve the encapsulated content as a particular type. The content is       * available as a result type if the message is outbound. The content       * is available as a source type if message is inbound. If the content is       * not available as the specified type null is returned.      *       * @param format the expected content format       * @return the encapsulated content      */
parameter_list|<
name|T
parameter_list|>
name|T
name|getContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|)
function_decl|;
comment|/**      * Provide the encapsulated content as a particular type (a result type      * if message is outbound, a source type if message is inbound)      *       * @param format the provided content format       * @param content the content to be encapsulated      */
parameter_list|<
name|T
parameter_list|>
name|void
name|setContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|,
name|Object
name|content
parameter_list|)
function_decl|;
comment|/**      * @return the set of currently encapsulated content formats      */
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getContentFormats
parameter_list|()
function_decl|;
comment|/**      * Removes a content from a message.  If some contents are completely consumed,      * removing them is a good idea      * @param format the format to remove      */
parameter_list|<
name|T
parameter_list|>
name|void
name|removeContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|)
function_decl|;
comment|/**      * Queries the Message object's metadata for a specific property.      *       * @param key the Message interface's property strings that       * correlates to the desired property       * @return the property's value      */
name|Object
name|getContextualProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
name|void
name|resetContextCache
parameter_list|()
function_decl|;
name|void
name|setContextualProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|v
parameter_list|)
function_decl|;
comment|/**      * @return set of defined contextual property keys      */
name|Set
argument_list|<
name|String
argument_list|>
name|getContextualPropertyKeys
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

