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
name|SOAPEnvelope
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
name|SOAPException
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|helpers
operator|.
name|MapNamespaceContext
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
name|wss4j
operator|.
name|common
operator|.
name|ext
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_comment
comment|/**  * Utility to enable the checking of WS-Security signature/encryption  * coverage based on the results of the WSS4J processors.  This interceptor  * provides an alternative to using WS-Policy based configuration for crypto  * coverage enforcement.  */
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
argument_list|<>
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|checkFaults
init|=
literal|true
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
if|if
condition|(
name|this
operator|.
name|xPaths
operator|==
literal|null
operator|||
name|this
operator|.
name|xPaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// return
block|}
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
literal|"Error obtaining SOAP document"
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
throw|;
block|}
name|Element
name|documentElement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|SOAPMessage
name|saajDoc
init|=
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|SOAPEnvelope
name|envelope
init|=
name|saajDoc
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|checkFaults
operator|&&
name|envelope
operator|.
name|getBody
argument_list|()
operator|.
name|hasFault
argument_list|()
condition|)
block|{
return|return;
block|}
name|documentElement
operator|=
name|envelope
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
literal|"Error obtaining SOAP document"
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
throw|;
block|}
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|signed
init|=
operator|new
name|HashSet
argument_list|<>
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
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSHandlerResult
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
comment|// Get all encrypted and signed references
for|for
control|(
name|WSHandlerResult
name|wshr
range|:
name|results
control|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
name|wshr
operator|.
name|getActionResults
argument_list|()
operator|.
name|get
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|)
decl_stmt|;
if|if
condition|(
name|signedResults
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signedResults
control|)
block|{
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
name|signedResult
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
continue|continue;
block|}
name|signed
operator|.
name|addAll
argument_list|(
name|sl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
init|=
name|wshr
operator|.
name|getActionResults
argument_list|()
operator|.
name|get
argument_list|(
name|WSConstants
operator|.
name|ENCR
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptedResults
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|encryptedResult
range|:
name|encryptedResults
control|)
block|{
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
name|encryptedResult
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
name|encrypted
operator|.
name|addAll
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|CryptoCoverageUtil
operator|.
name|reconcileEncryptedSignedRefs
argument_list|(
name|signed
argument_list|,
name|encrypted
argument_list|)
expr_stmt|;
comment|// XPathFactory and XPath are not thread-safe so we must recreate them
comment|// each request.
specifier|final
name|XPathFactory
name|factory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|final
name|XPath
name|xpath
init|=
name|factory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|prefixMap
operator|!=
literal|null
condition|)
block|{
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|MapNamespaceContext
argument_list|(
name|this
operator|.
name|prefixMap
argument_list|)
argument_list|)
expr_stmt|;
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
name|documentElement
argument_list|,
name|refsToCheck
argument_list|,
name|xpath
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|xPathExpression
operator|.
name|getXPath
argument_list|()
argument_list|)
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
name|xpaths
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
name|xpaths
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
name|xpaths
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds the XPath expressions to check for, adding to any previously      * set expressions.      *      * @param xPaths the XPath expressions to check for      */
specifier|public
specifier|final
name|void
name|addXPaths
parameter_list|(
name|List
argument_list|<
name|XPathExpression
argument_list|>
name|xpaths
parameter_list|)
block|{
if|if
condition|(
name|xpaths
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
name|xpaths
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
comment|/**      * Adds the mapping of namespace prefixes to namespace URIs, adding to any previously      * set mappings.      *      * @param prefixes the mapping of namespace prefixes to namespace URIs      */
specifier|public
specifier|final
name|void
name|addPrefixes
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
specifier|public
name|boolean
name|isCheckFaults
parameter_list|()
block|{
return|return
name|checkFaults
return|;
block|}
specifier|public
name|void
name|setCheckFaults
parameter_list|(
name|boolean
name|checkFaults
parameter_list|)
block|{
name|this
operator|.
name|checkFaults
operator|=
name|checkFaults
expr_stmt|;
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
comment|/**          * Create a new expression indicating a cryptographic coverage          * requirement with {@code scope} {@link CoverageScope#ELEMENT}.          *           * @param xPath          *            the XPath expression          * @param type          *            the type of coverage that the expression is meant to          *            enforce          *           * @throws NullPointerException          *             if {@code xPath} or {@code type} is {@code null}          */
specifier|public
name|XPathExpression
parameter_list|(
name|String
name|xPath
parameter_list|,
name|CoverageType
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|xPath
argument_list|,
name|type
argument_list|,
name|CoverageScope
operator|.
name|ELEMENT
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|xpathObject
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|xpathObject
operator|instanceof
name|XPathExpression
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|xpathObject
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
name|XPathExpression
name|xpath
init|=
operator|(
name|XPathExpression
operator|)
name|xpathObject
decl_stmt|;
if|if
condition|(
name|xpath
operator|.
name|getScope
argument_list|()
operator|!=
name|getScope
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|xpath
operator|.
name|getType
argument_list|()
operator|!=
name|getType
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getXPath
argument_list|()
operator|==
literal|null
operator|&&
name|xpath
operator|.
name|getXPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|getXPath
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getXPath
argument_list|()
operator|.
name|equals
argument_list|(
name|xpath
operator|.
name|getXPath
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
if|if
condition|(
name|getXPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getXPath
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getType
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getScope
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

