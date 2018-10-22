begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|performance
operator|.
name|complex_type
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|logging
operator|.
name|Level
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
name|xml
operator|.
name|datatype
operator|.
name|DatatypeConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|DatatypeFactory
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
name|ws
operator|.
name|Holder
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
name|frontend
operator|.
name|ClientProxy
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
name|http
operator|.
name|HTTPConduit
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|pat
operator|.
name|internal
operator|.
name|TestCaseBase
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
name|pat
operator|.
name|internal
operator|.
name|TestResult
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
name|cxf
operator|.
name|performance
operator|.
name|DocPortType
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
name|cxf
operator|.
name|performance
operator|.
name|DocPortTypeWrapped
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
name|cxf
operator|.
name|performance
operator|.
name|RPCPortType
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
name|cxf
operator|.
name|performance
operator|.
name|PerfService
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
name|cxf
operator|.
name|performance
operator|.
name|types
operator|.
name|ColourEnum
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
name|cxf
operator|.
name|performance
operator|.
name|types
operator|.
name|NestedComplexType
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
name|cxf
operator|.
name|performance
operator|.
name|types
operator|.
name|NestedComplexTypeSeq
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
name|cxf
operator|.
name|performance
operator|.
name|types
operator|.
name|SimpleStruct
import|;
end_import

begin_comment
comment|//import org.apache.cxf.cxf.performance.server.Server;
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|performance
operator|.
name|complex_type
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
extends|extends
name|TestCaseBase
argument_list|<
name|DocPortType
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/cxf/performance"
argument_list|,
literal|"PerfService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/cxf/performance"
argument_list|,
literal|"DocPortType"
argument_list|)
decl_stmt|;
specifier|private
name|PerfService
name|cs
decl_stmt|;
specifier|private
specifier|final
name|NestedComplexTypeSeq
name|complexTypeSeq
init|=
operator|new
name|NestedComplexTypeSeq
argument_list|()
decl_stmt|;
specifier|private
name|int
name|opid
decl_stmt|;
specifier|private
name|byte
index|[]
name|inputBase64
decl_stmt|;
specifier|private
name|String
name|inputString
init|=
operator|new
name|String
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|int
name|statId
decl_stmt|;
specifier|private
specifier|final
name|int
name|asciiCount
init|=
literal|1
operator|*
literal|1024
decl_stmt|;
specifier|public
name|Client
parameter_list|(
name|String
index|[]
name|args
parameter_list|,
name|boolean
name|warmup
parameter_list|)
block|{
name|super
argument_list|(
literal|"Base TestCase"
argument_list|,
name|args
argument_list|,
name|warmup
argument_list|)
expr_stmt|;
name|wsdlPath
operator|=
name|PerfService
operator|.
name|WSDL_LOCATION
operator|.
name|toString
argument_list|()
expr_stmt|;
name|serviceName
operator|=
name|SERVICE_NAME
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
name|portName
operator|=
name|PORT_NAME
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
name|operationName
operator|=
literal|"echoComplexTypeDoc"
expr_stmt|;
name|wsdlNameSpace
operator|=
literal|"http://cxf.apache.org/cxf/performance"
expr_stmt|;
name|amount
operator|=
literal|30
expr_stmt|;
name|packetSize
operator|=
literal|1
expr_stmt|;
name|usingTime
operator|=
literal|true
expr_stmt|;
name|numberOfThreads
operator|=
literal|4
expr_stmt|;
block|}
specifier|public
name|void
name|processArgs
parameter_list|()
block|{
name|super
operator|.
name|processArgs
argument_list|()
expr_stmt|;
if|if
condition|(
name|getOperationName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"echoStringDoc"
argument_list|)
condition|)
block|{
name|opid
operator|=
literal|0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getOperationName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"echoBase64Doc"
argument_list|)
condition|)
block|{
name|opid
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getOperationName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"echoComplexTypeDoc"
argument_list|)
condition|)
block|{
name|opid
operator|=
literal|2
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invalid operation: "
operator|+
name|getOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
comment|//workaround issue of xmlsec logging too much
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"org.apache.xml.security.signature.Reference"
argument_list|)
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
expr_stmt|;
name|int
name|threadIdx
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|servIdx
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|args
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
literal|"-Threads"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|threadIdx
operator|=
name|x
operator|+
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-Server"
operator|.
name|equals
argument_list|(
name|args
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|servIdx
operator|=
name|x
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|servIdx
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|tmp
index|[]
init|=
operator|new
name|String
index|[
name|args
operator|.
name|length
operator|-
name|servIdx
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|args
argument_list|,
name|servIdx
argument_list|,
name|tmp
argument_list|,
literal|0
argument_list|,
name|args
operator|.
name|length
operator|-
name|servIdx
argument_list|)
expr_stmt|;
name|Server
operator|.
name|main
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|tmp
operator|=
operator|new
name|String
index|[
name|servIdx
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|args
argument_list|,
literal|0
argument_list|,
name|tmp
argument_list|,
literal|0
argument_list|,
name|servIdx
argument_list|)
expr_stmt|;
name|args
operator|=
name|tmp
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|threadList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|threadIdx
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|threads
index|[]
init|=
name|args
index|[
name|threadIdx
index|]
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|threads
control|)
block|{
if|if
condition|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|s1
init|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|s2
init|=
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|i1
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|int
name|i2
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
name|i1
init|;
name|x
operator|<=
name|i2
condition|;
name|x
operator|++
control|)
block|{
name|threadList
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|threadList
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|threadList
operator|.
name|add
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|threadList
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|numThreads
range|:
name|threadList
control|)
block|{
if|if
condition|(
name|threadIdx
operator|!=
operator|-
literal|1
condition|)
block|{
name|args
index|[
name|threadIdx
index|]
operator|=
name|numThreads
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
name|args
argument_list|,
name|first
argument_list|)
decl_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
name|client
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|client
operator|.
name|run
argument_list|()
expr_stmt|;
name|List
name|results
init|=
name|client
operator|.
name|getTestResults
argument_list|()
decl_stmt|;
name|TestResult
name|testResult
init|=
literal|null
decl_stmt|;
name|double
name|rt
init|=
literal|0.0
decl_stmt|;
name|double
name|tp
init|=
literal|0.0
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|results
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|testResult
operator|=
operator|(
name|TestResult
operator|)
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Throughput "
operator|+
name|testResult
operator|.
name|getThroughput
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"AVG Response Time "
operator|+
name|testResult
operator|.
name|getAvgResponseTime
argument_list|()
argument_list|)
expr_stmt|;
name|rt
operator|+=
name|testResult
operator|.
name|getAvgResponseTime
argument_list|()
expr_stmt|;
name|tp
operator|+=
name|testResult
operator|.
name|getThroughput
argument_list|()
expr_stmt|;
block|}
name|rt
operator|*=
literal|1000
expr_stmt|;
name|rt
operator|/=
operator|(
name|double
operator|)
name|results
operator|.
name|size
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Total("
operator|+
name|numThreads
operator|+
literal|"):  "
operator|+
name|tp
operator|+
literal|" tps     "
operator|+
name|rt
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"cxf client is going to shutdown!"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SimpleStruct
name|getSimpleStruct
parameter_list|()
throws|throws
name|DatatypeConfigurationException
block|{
name|SimpleStruct
name|ss
init|=
operator|new
name|SimpleStruct
argument_list|()
decl_stmt|;
name|ss
operator|.
name|setVarFloat
argument_list|(
name|Float
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarShort
argument_list|(
name|Short
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarByte
argument_list|(
name|Byte
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarDecimal
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"3.1415926"
argument_list|)
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarDouble
argument_list|(
name|Double
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarString
argument_list|(
literal|"1234567890!@#$%^&*()abcdefghijk"
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarAttrString
argument_list|(
literal|"1234567890!@#$%^&*()abcdefghijk"
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarDateTime
argument_list|(
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXMLGregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|12
argument_list|,
literal|3
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|9
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ss
return|;
block|}
specifier|private
name|NestedComplexType
name|createComplexType
parameter_list|()
block|{
name|NestedComplexType
name|complexType
init|=
operator|new
name|NestedComplexType
argument_list|()
decl_stmt|;
name|complexType
operator|.
name|setVarString
argument_list|(
literal|"#12345ABc"
argument_list|)
expr_stmt|;
name|complexType
operator|.
name|setVarUByte
argument_list|(
operator|(
name|short
operator|)
literal|255
argument_list|)
expr_stmt|;
name|complexType
operator|.
name|setVarUnsignedLong
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"13691056728"
argument_list|)
argument_list|)
expr_stmt|;
name|complexType
operator|.
name|setVarFloat
argument_list|(
name|Float
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|complexType
operator|.
name|setVarQName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"return"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|complexType
operator|.
name|setVarStruct
argument_list|(
name|getSimpleStruct
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DatatypeConfigurationException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|complexType
operator|.
name|setVarEnum
argument_list|(
name|ColourEnum
operator|.
name|RED
argument_list|)
expr_stmt|;
name|byte
index|[]
name|binary
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
for|for
control|(
name|int
name|jdx
init|=
literal|0
init|;
name|jdx
operator|<
literal|256
condition|;
name|jdx
operator|++
control|)
block|{
name|binary
index|[
name|jdx
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|jdx
operator|-
literal|128
argument_list|)
expr_stmt|;
block|}
name|complexType
operator|.
name|setVarHexBinary
argument_list|(
name|binary
argument_list|)
expr_stmt|;
name|complexType
operator|.
name|setVarBase64Binary
argument_list|(
name|binary
argument_list|)
expr_stmt|;
return|return
name|complexType
return|;
block|}
specifier|public
name|void
name|initTestData
parameter_list|()
block|{
name|NestedComplexType
name|ct
init|=
name|createComplexType
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|packetSize
condition|;
name|i
operator|++
control|)
block|{
name|complexTypeSeq
operator|.
name|getItem
argument_list|()
operator|.
name|add
argument_list|(
name|ct
argument_list|)
expr_stmt|;
block|}
comment|// init String and Binary
name|String
name|temp
init|=
literal|"abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+?><[]/0123456789"
decl_stmt|;
name|inputBase64
operator|=
operator|new
name|byte
index|[
literal|1024
index|]
expr_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|4
condition|;
name|idx
operator|++
control|)
block|{
for|for
control|(
name|int
name|jdx
init|=
literal|0
init|;
name|jdx
operator|<
literal|256
condition|;
name|jdx
operator|++
control|)
block|{
name|inputBase64
index|[
name|idx
operator|*
literal|256
operator|+
name|jdx
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|jdx
operator|-
literal|128
argument_list|)
expr_stmt|;
block|}
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|packetSize
operator|*
literal|1024
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|inputString
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|asciiCount
operator|/
name|temp
operator|.
name|length
argument_list|()
operator|*
name|packetSize
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|temp
argument_list|)
expr_stmt|;
block|}
name|inputString
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|doJob
parameter_list|(
name|DocPortType
name|port
parameter_list|)
block|{
try|try
block|{
switch|switch
condition|(
name|opid
condition|)
block|{
case|case
literal|0
case|:
name|port
operator|.
name|echoStringDoc
argument_list|(
name|inputString
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|port
operator|.
name|echoBase64Doc
argument_list|(
name|inputBase64
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|int
name|id
init|=
operator|++
name|statId
decl_stmt|;
name|Holder
argument_list|<
name|Integer
argument_list|>
name|i
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|port
operator|.
name|echoComplexTypeDoc
argument_list|(
name|complexTypeSeq
argument_list|,
name|id
argument_list|,
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|id
operator|!=
name|i
operator|.
name|value
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|id
operator|+
literal|" != "
operator|+
name|i
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|port
operator|.
name|echoComplexTypeDoc
argument_list|(
name|complexTypeSeq
argument_list|,
literal|0
argument_list|,
operator|new
name|Holder
argument_list|<
name|Integer
argument_list|>
argument_list|()
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
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|DocPortType
name|getPort
parameter_list|()
block|{
try|try
block|{
name|URL
name|wsdl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wsdlPath
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
operator|||
name|wsdlPath
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|||
name|wsdlPath
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
condition|)
block|{
name|wsdl
operator|=
operator|new
name|URL
argument_list|(
name|wsdlPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wsdl
operator|=
operator|new
name|URL
argument_list|(
literal|"file://"
operator|+
name|wsdlPath
argument_list|)
expr_stmt|;
block|}
name|cs
operator|=
operator|new
name|PerfService
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_NAME
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|DocPortType
name|port
init|=
name|cs
operator|.
name|getSoapHttpDocLitPort
argument_list|()
decl_stmt|;
comment|/*         org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);         HTTPConduit http = (HTTPConduit) client.getConduit();          HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();         //httpClientPolicy.setAllowChunking(false);          http.setClient(httpClientPolicy);         */
return|return
name|port
return|;
block|}
specifier|public
name|void
name|printUsage
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Syntax is: Client [-WSDL wsdllocation] [-PacketSize packetnumber] "
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

