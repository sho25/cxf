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
name|ws
operator|.
name|security
operator|.
name|wss4j
package|;
end_package

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
name|Collection
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Vector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|cxf
operator|.
name|interceptor
operator|.
name|Fault
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
name|phase
operator|.
name|Phase
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|CryptoCoverageUtil
operator|.
name|CoverageScope
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|CryptoCoverageUtil
operator|.
name|CoverageType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSDataRef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|handler
operator|.
name|WSHandlerConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_comment
comment|/**  * Utility to enable the checking of WS-Security signature/encryption  * coverage based on the results of the WSS4J processors.  This interceptor  * provides an alternative to using WS-Policy based configuration for crypto  * coverage enforcement.  *<p/>  * Note that the processor must properly address the Security Token  * Reference Dereference transform in the case of a signed security token  * such as a SAML assertion.  Consequently, a version of WSS4J that properly  * addresses this transform must be used with this utility if you wish to   * check coverage over a message part referenced through the Security Token  * Reference Dereference transform.  * See<a href="https://issues.apache.org/jira/browse/WSS-222">WSS-222</a>  * for more details.  */
end_comment

begin_class
specifier|public
class|class
name|CryptoCoverageChecker
extends|extends
name|AbstractSoapInterceptor
block|{
comment|/**      * The XPath expressions for locating elements in SOAP messages      * that must be covered.  See {@link #prefixMap}      * for namespace prefixes available.      */
specifier|protected
name|List
argument_list|<
name|XPathExpression
argument_list|>
name|xPaths
init|=
operator|new
name|ArrayList
argument_list|<
name|XPathExpression
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Mapping of namespace prefixes to namespace URIs.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefixMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new instance.  See {@link #setPrefixes()} and {@link #setXpaths()}      * for providing configuration options.      */
specifier|public
name|CryptoCoverageChecker
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance that checks for signature coverage over matches to      * the provided XPath expressions making defensive copies of provided arguments.      *       * @param prefixes      *            mapping of namespace prefixes to namespace URIs      * @param xPaths      *            a list of XPath expressions      */
specifier|public
name|CryptoCoverageChecker
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefixes
parameter_list|,
name|List
argument_list|<
name|XPathExpression
argument_list|>
name|xPaths
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|this
operator|.
name|addAfter
argument_list|(
name|WSS4JInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPrefixes
argument_list|(
name|prefixes
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXPaths
argument_list|(
name|xPaths
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks that the WSS4J results refer to the required signed/encrypted      * elements as defined by the XPath expressions in {@link #xPaths}.      *       * @param message      *            the SOAP message containing the signature      *       * @throws SoapFault      *             if there is an error evaluating an XPath or an element is not      *             covered by the required cryptographic operation      */
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|signed
init|=
operator|new
name|HashSet
argument_list|<
name|WSDataRef
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|encrypted
init|=
operator|new
name|HashSet
argument_list|<
name|WSDataRef
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|results
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|result
range|:
name|results
control|)
block|{
specifier|final
name|WSHandlerResult
name|wshr
init|=
operator|(
name|WSHandlerResult
operator|)
name|result
decl_stmt|;
specifier|final
name|Vector
argument_list|<
name|Object
argument_list|>
name|wsSecurityEngineSignResults
init|=
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Vector
argument_list|<
name|Object
argument_list|>
name|wsSecurityEngineEncResults
init|=
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|wshr
operator|.
name|getResults
argument_list|()
argument_list|,
name|WSConstants
operator|.
name|SIGN
argument_list|,
name|wsSecurityEngineSignResults
argument_list|)
expr_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|wshr
operator|.
name|getResults
argument_list|()
argument_list|,
name|WSConstants
operator|.
name|ENCR
argument_list|,
name|wsSecurityEngineEncResults
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|wsSecurityEngineSignResults
control|)
block|{
name|WSSecurityEngineResult
name|wser
init|=
operator|(
name|WSSecurityEngineResult
operator|)
name|o
decl_stmt|;
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|sl
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|sl
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|sl
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_LN
argument_list|)
argument_list|)
condition|)
block|{
comment|//endorsing the signature so don't include
break|break;
block|}
for|for
control|(
name|WSDataRef
name|r
range|:
name|sl
control|)
block|{
name|signed
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Object
name|o
range|:
name|wsSecurityEngineEncResults
control|)
block|{
name|WSSecurityEngineResult
name|wser
init|=
operator|(
name|WSSecurityEngineResult
operator|)
name|o
decl_stmt|;
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|el
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSDataRef
name|r
range|:
name|el
control|)
block|{
name|encrypted
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
for|for
control|(
name|XPathExpression
name|xPathExpression
range|:
name|this
operator|.
name|xPaths
control|)
block|{
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refsToCheck
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|xPathExpression
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|SIGNED
case|:
name|refsToCheck
operator|=
name|signed
expr_stmt|;
break|break;
case|case
name|ENCRYPTED
case|:
name|refsToCheck
operator|=
name|encrypted
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected crypto type: "
operator|+
name|xPathExpression
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
try|try
block|{
name|CryptoCoverageUtil
operator|.
name|checkCoverage
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
argument_list|,
name|refsToCheck
argument_list|,
name|this
operator|.
name|prefixMap
argument_list|,
name|xPathExpression
operator|.
name|getXPath
argument_list|()
argument_list|,
name|xPathExpression
operator|.
name|getType
argument_list|()
argument_list|,
name|xPathExpression
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
literal|"No "
operator|+
name|xPathExpression
operator|.
name|getType
argument_list|()
operator|+
literal|" element found matching XPath "
operator|+
name|xPathExpression
operator|.
name|getXPath
argument_list|()
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Sets the XPath expressions to check for, clearing all previously      * set expressions.      *      * @param xPaths the XPath expressions to check for      */
specifier|public
specifier|final
name|void
name|setXPaths
parameter_list|(
name|List
argument_list|<
name|XPathExpression
argument_list|>
name|xPaths
parameter_list|)
block|{
name|this
operator|.
name|xPaths
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|xPaths
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|xPaths
operator|.
name|addAll
argument_list|(
name|xPaths
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the mapping of namespace prefixes to namespace URIs, clearing all previously      * set mappings.      *      * @param prefixes the mapping of namespace prefixes to namespace URIs      */
specifier|public
specifier|final
name|void
name|setPrefixes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefixes
parameter_list|)
block|{
name|this
operator|.
name|prefixMap
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|prefixes
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|prefixMap
operator|.
name|putAll
argument_list|(
name|prefixes
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A simple wrapper for an XPath expression and coverage type / scope      * indicating how the XPath expression should be enforced as a cryptographic      * coverage requirement.      */
specifier|public
specifier|static
class|class
name|XPathExpression
block|{
comment|/**          * The XPath expression.          */
specifier|private
specifier|final
name|String
name|xPath
decl_stmt|;
comment|/**          * The type of coverage that is being enforced.          */
specifier|private
specifier|final
name|CoverageType
name|type
decl_stmt|;
comment|/**          * The scope of the coverage that is being enforced.          */
specifier|private
specifier|final
name|CoverageScope
name|scope
decl_stmt|;
comment|/**          * Create a new expression indicating a cryptographic coverage          * requirement. If {@code type} is {@link CoverageType#SIGNED}, the          * {@code scope} {@link CoverageScope#CONTENT} does not represent a          * configuration supported in WS-Security.          *           * @param xPath          *            the XPath expression          * @param type          *            the type of coverage that the expression is meant to          *            enforce          * @param scope          *            the scope of coverage that the expression is meant to          *            enforce, defaults to {@link CoverageScope#ELEMENT}          *           * @throws NullPointerException          *             if {@code xPath} or {@code type} is {@code null}          */
specifier|public
name|XPathExpression
parameter_list|(
name|String
name|xPath
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
block|{
if|if
condition|(
name|xPath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"xPath cannot be null."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"type cannot be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|xPath
operator|=
name|xPath
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
comment|/**          * Returns the XPath expression.          * @return the XPath expression          */
specifier|public
name|String
name|getXPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|xPath
return|;
block|}
comment|/**          * Returns the coverage type.          * @return the coverage type          */
specifier|public
name|CoverageType
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
return|;
block|}
comment|/**          * Returns the coverage scope.          * @return the coverage scope          */
specifier|public
name|CoverageScope
name|getScope
parameter_list|()
block|{
return|return
name|this
operator|.
name|scope
return|;
block|}
block|}
block|}
end_class

end_unit

